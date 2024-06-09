package com.example.localguidebe.controller;

import com.example.localguidebe.converter.CartToCartDtoConverter;
import com.example.localguidebe.dto.CartDTO;
import com.example.localguidebe.dto.requestdto.AddBookingRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateBookingDTO;
import com.example.localguidebe.entity.Cart;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.CartService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
  private final CartService cartService;
  private final CartToCartDtoConverter cartToCartDtoConverter;

  @Autowired
  public CartController(CartService cartService, CartToCartDtoConverter cartToCartDtoConverter) {
    this.cartService = cartService;
    this.cartToCartDtoConverter = cartToCartDtoConverter;
  }

  @GetMapping("")
  public ResponseEntity<Result> getTourInCartByUser(
      Authentication authentication,
      @RequestParam(value = "isDeleted", required = false, defaultValue = "false")
          boolean isDeleted) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          Cart cart =
              cartService.getCartWithUnPaidBooKingByEmail(
                  ((CustomUserDetails) authentication.getPrincipal()).getEmail());
          if (cart == null) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.NOT_FOUND.value(),
                        "You haven't added any tours to the cart yet."));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Get cart successfully",
                      cartToCartDtoConverter.convert(cart, isDeleted)));
        });
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result> deleteBookingInCartByUser(
      Authentication authentication, @PathVariable Long id) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          boolean isDeleted =
              cartService.deleteBookingInCartByIdAndByEmail(
                  ((CustomUserDetails) authentication.getPrincipal()).getEmail(), id);
          if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(true, HttpStatus.NOT_FOUND.value(), "Can't delete this booking"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(new Result(true, HttpStatus.OK.value(), "Delete booking in cart successfully"));
        });
  }

  @PutMapping("")
  public ResponseEntity<Result> updateBookingInCartByUser(
      Authentication authentication, @RequestBody UpdateBookingDTO updateBookingDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            Cart updatedCart =
                cartService.updateBookingInCart(
                    ((CustomUserDetails) authentication.getPrincipal()).getEmail(),
                    updateBookingDTO);
            if (updatedCart == null) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(
                      new Result(
                          false,
                          HttpStatus.INTERNAL_SERVER_ERROR.value(),
                          "Update booking information failed"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Update booking information successfully",
                        cartToCartDtoConverter.convert(updatedCart, false)));
          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Result(false, HttpStatus.CONFLICT.value(), e.getMessage()));
          }
        });
  }

  @PostMapping("")
  public ResponseEntity<Result> addBookingToCart(
      Authentication authentication, @RequestBody AddBookingRequestDTO addBookingRequestDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            CartDTO addCart =
                cartService.addBookingInCart(
                    ((CustomUserDetails) authentication.getPrincipal()).getEmail(),
                    addBookingRequestDTO);
            if (addCart != null) {
              return new ResponseEntity<>(
                  new Result(
                      false, HttpStatus.OK.value(), "successfully added tour to cart", addCart),
                  HttpStatus.OK);
            } else {
              return new ResponseEntity<>(
                  new Result(
                      false, HttpStatus.CONFLICT.value(), "Adding tour to cart failed", null),
                  HttpStatus.CONFLICT);
            }
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(false, HttpStatus.CONFLICT.value(), e.getMessage(), null),
                HttpStatus.CONFLICT);
          }
        });
  }
}
