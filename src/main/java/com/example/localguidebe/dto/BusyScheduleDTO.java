package com.example.localguidebe.dto;

import com.example.localguidebe.enums.TypeBusyDayEnum;

import java.time.LocalDateTime;

public record BusyScheduleDTO(Long id, LocalDateTime busyDate, TypeBusyDayEnum typeBusyDayEnum) {}
