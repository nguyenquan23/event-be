package com.example.localguidebe.repository;

import com.example.localguidebe.entity.TourStartTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TourStartTimeRepository extends JpaRepository<TourStartTime, Integer> {
  @Query("SELECT tst.startTime FROM TourStartTime tst WHERE tst.tour.id = :tourId")
  List<String> findByTourId(@Param("tourId") Long tourId);
}
