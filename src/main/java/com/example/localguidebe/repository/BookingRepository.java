package com.example.localguidebe.repository;

import com.example.localguidebe.dto.ProvinceResponseDTO;
import com.example.localguidebe.dto.StatisticalBookingDTO;
import com.example.localguidebe.dto.TourRevenueDTO;
import com.example.localguidebe.entity.Booking;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  /*
  TODO: Consider whether to retrieve paid bookings or all bookings.
        If retrieving all bookings, set a timeout for pending_payment bookings.
   */
  @Query(
      "SELECT FUNCTION('time', b.startDate) FROM Booking b WHERE b.tour.id = :tourId AND FUNCTION('date', b.startDate) = :startDate")
  List<String> findStartDateTimesByTourIdAndStartDate(
      @Param("tourId") Long tourId, @Param("startDate") LocalDate startDate);

  @Modifying
  @Query("update Booking b set b.status = 'PAID' where b.id = :bookingId")
  void setBookingStatusToPaid(@Param("bookingId") Long bookingId);

  @Modifying
  @Query("update Booking b set b.isDeleted = true where b.id = :id")
  void deleteBookingById(@Param("id") Long id);

  @Query("SELECT b FROM Booking b JOIN b.tour t JOIN t.guide g WHERE g.email = :email")
  List<Booking> getBookingsOfGuide(@Param("email") String email);

  @Query(
      "SELECT b FROM Booking b JOIN b.cart cart WHERE  cart.traveler.id = :travelerId AND b.status ='PAID' ")
  List<Booking> getBookingHistory(@Param("travelerId") Long travelerId);

  @Query(
      "SELECT b FROM Booking b JOIN b.tour t JOIN t.guide g WHERE g.id =:guideId AND b.status = 'PAID'")
  List<Booking> getPaidBookingForGuide(Long guideId);

  @Query(
      "SELECT  NEW com.example.localguidebe.dto.ProvinceResponseDTO("
          + "SUBSTRING_INDEX(lt.address, ', ', -2),"
          + " COUNT(*)) "
          + "FROM Booking b "
          + "JOIN b.tour t "
          + "JOIN t.locations lt "
          + "WHERE b.status = 'PAID'"
          + "GROUP BY SUBSTRING_INDEX(lt.address, ', ', -2) "
          + "ORDER BY COUNT(*) DESC ")
  List<ProvinceResponseDTO> FindForSuggestedTours();

  @Query(
      "SELECT NEW com.example.localguidebe.dto.StatisticalBookingDTO("
          + "SUBSTRING_INDEX(location.address, ', ', -2), "
          + "SUM(booking.price), "
          + "COUNT(*)) "
          + "FROM Booking booking "
          + "JOIN booking.tour tour "
          + "JOIN tour.locations location "
          + "WHERE booking.status = 'PAID'"
          + "GROUP BY SUBSTRING_INDEX(location.address, ', ', -2)")
  List<StatisticalBookingDTO> getStatisticalBooking();

  @Query(
      "SELECT NEW com.example.localguidebe.dto.TourRevenueDTO("
          + "sum(booking.price) ,booking.tour.id,tour.name , sum(booking.numberTravelers)) "
          + " FROM Booking booking JOIN booking.tour tour"
          + " WHERE booking.status ='PAID' "
          + "GROUP BY booking.tour.id")
  List<TourRevenueDTO> getRevenueByTour();

  @Query(
      "SELECT sum(booking.price) FROM Booking booking WHERE booking.tour.id = :tourId AND booking.status = 'PAID'")
  Double getRevenueByTourId(Long tourId);

  @Query("SELECT tour.id FROM User user JOIN user.tours tour  WHERE user.id = :guideId")
  Page<Long> getTourIdByGuide(Long guideId, Pageable pageable);

  @Query("SELECT tour.id FROM User user JOIN user.tours tour  WHERE user.id = :guideId")
  List<Long> getTourIdByGuide(Long guideId);

  @Query(
      "SELECT SUM(booking.numberTravelers) FROM Booking booking WHERE booking.tour.id = :tourId AND booking.status = 'PAID'")
  Long getTotalTravelerNumberByTour(Long tourId);

  @Query(
      "SELECT count(booking) FROM Booking booking WHERE booking.tour.id = :tourId AND booking.status ='PAID'")
  Long getTotalBookingByTour(Long tourId);

  @Query("SELECT booking FROM Booking booking WHERE booking.status = 'PAID'")
  List<Booking> getPaidBooking();
}
