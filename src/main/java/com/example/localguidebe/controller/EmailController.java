package com.example.localguidebe.controller;

import com.example.localguidebe.service.EmailService;
import com.example.localguidebe.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
  private EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @GetMapping("/send")
  public ResponseEntity<Result> sendToEmail(
      @RequestParam("cartId") Long cartId, @RequestParam("email") String email) {
    try {
      return new ResponseEntity<>(
          new Result(
              true,
              HttpStatus.OK.value(),
              "send email successfully",
              emailService.sendEmailToTraveler(cartId, email)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(false, HttpStatus.CONFLICT.value(), "Email sending failed", null),
          HttpStatus.CONFLICT);
    }
  }
}
