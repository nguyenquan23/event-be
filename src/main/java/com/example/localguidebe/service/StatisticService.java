package com.example.localguidebe.service;

import com.example.localguidebe.dto.StatisticByMonthDTO;
import com.example.localguidebe.dto.responsedto.*;
import java.util.List;

public interface StatisticService {
  StatisticalGuidePaginationDTO getStatisticalByGuide(Integer page, Integer limit, String order);

  Double getRevenueByGuide(Long guideId);

  Double getRevenueByTour(Long tourId);

  Long getTotalTravelerNumberByTour(Long tourId);

  Long getTotalTravelerNumberByGuide(Long guideId);

  StatisticalTourPaginationDTO getStatisticalByTour(Integer page, Integer limit, String order);

  Long getTotalBookingByTour(Long tourId);

  Long getTotalBookingByGuide(Long guideId);

  StatisticalTourDTO getStatisticByPerTour(Long tourId);

  StatisticalGuideDTO getStatisticByPerGuide(Long guideId);

  StatisticOfToursByGuidePaginationDTO getStatisticOfToursByGuide(
      Long guideId, Integer page, Integer limit, String order);

  StatisticByMonthDTO getStatisticByMonthForAdmin(Integer year);

  List<StatisticalGuideDTO> getTopRevenue();

  StatisticByMonthDTO getStatisticByMonthForGuide(Integer year, Long guideId);
}
