package com.example.localguidebe.service;

import com.example.localguidebe.dto.EmailDetails;
import com.example.localguidebe.entity.Booking;
import org.thymeleaf.context.Context;

public interface EmailService {
  boolean sendEmailToTraveler(Long cartId, String recipientEmail);

  boolean sendSimpleMail(EmailDetails details);

  boolean sendMimeMail(EmailDetails details, String templateName, Context context);

  boolean sendMailWithAttachment(EmailDetails details, String templateName, Context context);

  boolean sendEmailForNewBooking(Booking booking);

  boolean sendEmailForResetPassword(String email, String token);
}
