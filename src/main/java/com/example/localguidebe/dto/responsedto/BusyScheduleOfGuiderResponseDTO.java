package com.example.localguidebe.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusyScheduleOfGuiderResponseDTO{
  private List<LocalDateTime> busyDayOfGuider = new ArrayList<>();
  private List<LocalDateTime> busyDayByBooking = new ArrayList<>();
}
