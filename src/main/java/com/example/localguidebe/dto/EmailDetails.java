package com.example.localguidebe.dto;

import lombok.Builder;

@Builder
public record EmailDetails(String recipient, String msgBody, String subject, String attachment) {}
