package com.example.localguidebe.vnpay;

import com.example.localguidebe.converter.InvoiceToInvoiceDtoConverter;
import com.example.localguidebe.entity.Invoice;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.InvoiceService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/payment/")
public class VNPayResource {
  Logger logger = LoggerFactory.getLogger(VNPayResource.class);

  @Value("${frontend.host}")
  private String frontendHost;

  @Value("${backend.host}")
  private String backendHost;

  private final InvoiceService invoiceService;
  private final InvoiceToInvoiceDtoConverter invoiceToInvoiceDtoConverter;
  private final RestTemplate restTemplate;
  private final Gson gson;
  private final UserService userService;

  @Autowired
  public VNPayResource(
      InvoiceService invoiceService,
      InvoiceToInvoiceDtoConverter invoiceToInvoiceDtoConverter,
      RestTemplate restTemplate,
      Gson gson,
      UserService userService) {
    this.invoiceService = invoiceService;
    this.invoiceToInvoiceDtoConverter = invoiceToInvoiceDtoConverter;
    this.restTemplate = restTemplate;
    this.gson = gson;
    this.userService = userService;
  }

  public Double getUSDtoVNDRate() {
    String apiUrl = "https://v6.exchangerate-api.com/v6/84e2879a7ba29d1d713949f9/latest/USD";
    String response = restTemplate.getForObject(apiUrl, String.class);
    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
    JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");
    return rates.get("VND").getAsDouble();
  }

  @GetMapping("payment-callback")
  public void paymentCallback(
      @RequestParam Map<String, String> queryParams, HttpServletResponse response)
      throws IOException {
    String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
    String vnp_TxnRef = queryParams.get("vnp_TxnRef");
    String travelerEmail = queryParams.get("travelerEmail");

    String email = queryParams.get("email");
    String fullName = queryParams.get("fullName");
    String phone = queryParams.get("phone");

    List<Long> bookingIds =
        Arrays.stream(queryParams.get("bookingId").split(",")).map(Long::valueOf).toList();
    Double priceInVND = Double.parseDouble(queryParams.get("vnp_Amount")) / 100;
    Double usdVndRate = Double.parseDouble(queryParams.get("usd_vnd_Rate"));
    Double priceInUSD =
        BigDecimal.valueOf(priceInVND / usdVndRate).setScale(0, RoundingMode.HALF_UP).doubleValue();
    if (!bookingIds.isEmpty() && email != null) {
      if ("00".equals(vnp_ResponseCode)) {
        Invoice newInvoice =
            invoiceService.createBookingInInvoice(
                vnp_TxnRef,
                bookingIds,
                email,
                travelerEmail,
                fullName,
                phone,
                priceInUSD,
                priceInVND,
                usdVndRate);
        if (newInvoice == null) {
          logger.error("Giao dịch thất bại");
          response.sendRedirect(frontendHost + "/booking-fail");
        } else {
          logger.info("Giao dịch thành công");

          response.sendRedirect(frontendHost + "/booking-success/" + newInvoice.getId());
        }
      } else {
        logger.error("Giao dịch thất bại");
        response.sendRedirect(frontendHost + "/booking-fail");
      }
    }
  }

  @GetMapping("pay")
  public ResponseEntity<Result> getPay(
      Authentication authentication,
      @RequestParam("price") Long price,
      @RequestParam("bookingIds") List<Long> bookingIds,
      @RequestParam("email") String email,
      @RequestParam("fullName") String fullName,
      @RequestParam("phone") String phone) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          String vnp_Version = "2.1.0";
          String vnp_Command = "pay";
          String orderType = "other";

          Double usdVndRate = getUSDtoVNDRate();
          BigDecimal amount =
              BigDecimal.valueOf(price * usdVndRate)
                  .setScale(0, RoundingMode.HALF_UP)
                  .multiply(BigDecimal.valueOf(100));

          String vnp_TxnRef = Config.getRandomNumber(8);
          String vnp_IpAddr = "127.0.0.1";

          String vnp_TmnCode = Config.vnp_TmnCode;

          Map<String, String> vnp_Params = new HashMap<>();

          vnp_Params.put("vnp_Version", vnp_Version);
          vnp_Params.put("vnp_Command", vnp_Command);
          vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
          vnp_Params.put("vnp_Amount", String.valueOf(amount));
          vnp_Params.put("vnp_CurrCode", "VND");

          String bankCode = "NCB";
          //          vnp_Params.put("vnp_BankCode", bankCode);
          vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
          vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
          vnp_Params.put("vnp_OrderType", orderType);

          vnp_Params.put("vnp_Locale", "vn");

          String returnUrl =
              backendHost
                  + Config.vnp_ReturnUrl
                  + "?bookingId="
                  + bookingIds.stream().map(Object::toString).collect(Collectors.joining(","))
                  + "&travelerEmail="
                  + travelerEmail
                  + "&email="
                  + email
                  + "&fullName="
                  + fullName
                  + "&phone="
                  + phone
                  + "&usd_vnd_Rate="
                  + usdVndRate;

          vnp_Params.put("vnp_ReturnUrl", returnUrl);
          vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

          Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
          SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
          String vnp_CreateDate = formatter.format(cld.getTime());
          vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

          cld.add(Calendar.MINUTE, 15);
          String vnp_ExpireDate = formatter.format(cld.getTime());
          vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

          List fieldNames = new ArrayList(vnp_Params.keySet());
          Collections.sort(fieldNames);
          StringBuilder hashData = new StringBuilder();
          StringBuilder query = new StringBuilder();
          Iterator itr = fieldNames.iterator();
          while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
              // Build hash data
              hashData.append(fieldName);
              hashData.append('=');
              hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
              // Build query
              query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
              query.append('=');
              query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
              if (itr.hasNext()) {
                query.append('&');
                hashData.append('&');
              }
            }
          }
          String queryUrl = query.toString();
          String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
          queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
          String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true, HttpStatus.OK.value(), "Get payment url successfully", paymentUrl));
        });
  }

  @GetMapping("/refund")
  public ResponseEntity<Result> testRefund(
      Authentication authentication,
      @RequestParam("invoice_id") Long invoice_id,
      HttpServletRequest request) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User traveler = userService.findUserByEmail(travelerEmail);
          Invoice invoice =
              traveler.getInvoices().stream()
                  .filter(invoice1 -> invoice1.getId().equals(invoice_id))
                  .findFirst()
                  .orElse(null);
          if (invoice == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(true, HttpStatus.NOT_FOUND.value(), "Invoice is not exist"));

          Double refundAmount = invoiceService.getRefundAmount(invoice);
          if (refundAmount == 0.0)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                    new Result(
                        true, HttpStatus.CONFLICT.value(), "You can't be refund for this invoice"));
          String vnp_RequestId = Config.getRandomNumber(8);
          String vnp_Version = "2.1.0";
          String vnp_Command = "refund";
          String vnp_TmnCode = Config.vnp_TmnCode;
          String vnp_TransactionType = "02";
          String vnp_TxnRef = invoice.getVnpTxnRef();
          DecimalFormat decimalFormat = new DecimalFormat("#");

          // Because the refund price must be == pay price in sandbox environment
          String vnp_Amount = decimalFormat.format(invoice.getVndPrice()) + "00";
          String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
          String vnp_TransactionNo = "";
          String vnp_TransactionDate =
              invoice.getCreateAt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
          String vnp_CreateBy = traveler.getFullName();

          Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
          SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
          String vnp_CreateDate = formatter.format(cld.getTime());

          String vnp_IpAddr = Config.getIpAddress(request);

          JsonObject vnp_Params = new JsonObject();

          vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
          vnp_Params.addProperty("vnp_Version", vnp_Version);
          vnp_Params.addProperty("vnp_Command", vnp_Command);
          vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
          vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
          vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
          vnp_Params.addProperty("vnp_Amount", vnp_Amount);
          vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

          //          if (vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty()) {
          //            vnp_Params.addProperty("vnp_TransactionNo", "14351149");
          //            logger.info("!= null");
          //          }
          vnp_Params.addProperty("vnp_TransactionNo", vnp_TransactionNo);

          logger.info("vnp_TransactionNo" + vnp_Params.get("vnp_TransactionNo"));
          vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
          vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
          vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
          vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

          String hash_Data =
              String.join(
                  "|",
                  vnp_RequestId,
                  vnp_Version,
                  vnp_Command,
                  vnp_TmnCode,
                  vnp_TransactionType,
                  vnp_TxnRef,
                  vnp_Amount,
                  vnp_TransactionNo,
                  vnp_TransactionDate,
                  vnp_CreateBy,
                  vnp_CreateDate,
                  vnp_IpAddr,
                  vnp_OrderInfo);

          String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data);
          vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

          // make post request to Vn-pay to refund order
          URL url = null;
          try {
            url = new URL(Config.vnp_ApiUrl);
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }

          HttpURLConnection con = null;
          int responseCode;
          try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(vnp_Params.toString());
            wr.flush();
            wr.close();
            responseCode = con.getResponseCode();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          if (responseCode != 200) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                    new Result(
                        true,
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        "Refund order not successfully, please contact to guide"));
          }

          BufferedReader in = null;
          StringBuilder response = new StringBuilder();
          try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            while ((output = in.readLine()) != null) {
              response.append(output);
            }
            in.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }

          JsonObject refundResponse = gson.fromJson(String.valueOf(response), JsonObject.class);
          String vnp_ResponseCode = refundResponse.get("vnp_ResponseCode").getAsString();
          logger.info("vnp_Amount: " + vnp_Amount);
          logger.info("response code: " + vnp_ResponseCode);
          if (!vnp_ResponseCode.equals("00")) {
            String message =
                switch (vnp_ResponseCode) {
                  case "02" -> "Tổng số tiền hoản trả lớn hơn số tiền gốc";
                  case "03" -> "Dữ liệu gửi sang không đúng định dạng";
                  case "04" -> "Không cho phép hoàn trả toàn phần sau khi hoàn trả một phần";
                  case "13" -> "Chỉ cho phép hoàn trả một phần";
                  case "91" -> "Không tìm thấy giao dịch yêu cầu hoàn trả";
                  case "93" ->
                      "Số tiền hoàn trả không hợp lệ. Số tiền hoàn trả phải nhỏ hơn hoặc bằng số tiền thanh toán.";
                  case "94" ->
                      "Yêu cầu bị trùng lặp trong thời gian giới hạn của API (Giới hạn trong 5 phút)";
                  case "95" ->
                      "Giao dịch này không thành công bên VNPAY. VNPAY từ chối xử lý yêu cầu.";
                  case "97" -> "Chữ ký không hợp lệ";
                  case "98" -> "Timeout Exception";
                  case "99" ->
                      "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)";
                  default -> "";
                };
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Result(true, HttpStatus.CONFLICT.value(), message));
          }

          try {
            Invoice refundedInvoice = invoiceService.refundInvoice(invoice, refundAmount);
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Your order has been refunded",
                        invoiceToInvoiceDtoConverter.convert(refundedInvoice)));
          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        true,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Your order hasn't been refunded"));
          }
        });
  }
}
