package com.example.localguidebe.controller;

import com.example.localguidebe.converter.InvoiceToInvoiceDtoConverter;
import com.example.localguidebe.dto.InvoiceDTO;
import com.example.localguidebe.entity.Invoice;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.InvoiceService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
  private final InvoiceService invoiceService;
  private final InvoiceToInvoiceDtoConverter invoiceToInvoiceDtoConverter;
  private final UserService userService;

  public InvoiceController(
      InvoiceService invoiceService,
      InvoiceToInvoiceDtoConverter invoiceToInvoiceDtoConverter,
      UserService userService) {
    this.invoiceService = invoiceService;
    this.invoiceToInvoiceDtoConverter = invoiceToInvoiceDtoConverter;
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result> getInvoiceById(@PathVariable("id") Long id) {
    Optional<Invoice> invoice = invoiceService.findById(id);
    return invoice
        .map(
            value ->
                ResponseEntity.status(HttpStatus.OK)
                    .body(
                        new Result(
                            true,
                            HttpStatus.OK.value(),
                            "Get invoice successfully",
                            invoiceToInvoiceDtoConverter.convert(value))))
        .orElseGet(
            () ->
                ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new Result(false, HttpStatus.NO_CONTENT.value(), "Not found invoice")));
  }

  @GetMapping("")
  public ResponseEntity<Result> getInvoicesOfUser(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            User traveler = userService.findUserByEmail(travelerEmail);
            List<Invoice> invoices = traveler.getInvoices();
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Get invoices successfully",
                        invoices.stream()
                            .map(invoiceToInvoiceDtoConverter::convert)
                            .sorted(Comparator.comparing(InvoiceDTO::createAt).reversed())
                            .toList()));
          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Get invoices failed"));
          }
        });
  }
}
