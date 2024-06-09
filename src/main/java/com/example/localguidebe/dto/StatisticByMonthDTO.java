package com.example.localguidebe.dto;

import com.example.localguidebe.dto.MonthDTO;
import java.util.List;
import lombok.*;

@Getter
@Builder
public class StatisticByMonthDTO {
  private Integer year;
  private List<MonthDTO> monthDTOS;
}
