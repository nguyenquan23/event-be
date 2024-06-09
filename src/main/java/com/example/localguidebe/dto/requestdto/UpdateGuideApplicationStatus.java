package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.enums.GuideApplicationStatus;

public record UpdateGuideApplicationStatus(GuideApplicationStatus status, String reasonDeny) {}
