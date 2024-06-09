package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Review;
import com.example.localguidebe.entity.Tour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

  @Query(
      "SELECT distinct tour FROM Tour tour JOIN FETCH tour.categories category JOIN FETCH tour.locations locations WHERE( LOWER(tour.name) LIKE %:searchKey% OR LOWER(locations.address) LIKE %:searchKey%) AND tour.pricePerTraveler >= :minPrice AND tour.pricePerTraveler <= :maxPrice AND category.id IN :categoryId")
  Page<Tour> findTours(
      @Param("searchKey") String searchKey,
      @Param("minPrice") Double minPrice,
      @Param("maxPrice") Double maxPrice,
      @Param("categoryId") List<Long> categoryId,
      Pageable pageable);

  @Query(
      "SELECT tour from Tour tour JOIN tour.categories category WHERE tour.pricePerTraveler >= :minPrice AND tour.pricePerTraveler <= :maxPrice AND category.id IN :categoryId AND (tour.name IN :searchNames OR tour.address IN :addresses)")
  Page<Tour> findToursByNameAndAddress(
      @Param("searchNames") List<String> searchNames,
      @Param("minPrice") Double minPrice,
      Double maxPrice,
      List<Long> categoryId,
      Pageable pageable,
      List<String> addresses);

  @Query("SELECT tour FROM Tour tour JOIN tour.reviews review where review.id = :reviewId")
  Optional<Tour> findTourByReviewsId(@Param("reviewId") Long reviewId);

  @Query(
      "SELECT review FROM Tour tour JOIN tour.reviews review WHERE tour.id = :tourId AND review.rating IN :ratings ORDER BY CASE WHEN :sortBy = 'Most recent' THEN review.createdAt END DESC ,CASE WHEN :sortBy ='Highest rated' THEN review.rating END DESC ,CASE WHEN :sortBy = 'Lowest rated' THEN review.rating END ASC ")
  List<Review> filterReviewForTour(
      @Param("ratings") List<Integer> ratings,
      @Param("tourId") Long tourId,
      @Param("sortBy") String sortBy);

  @Query(
      "SELECT tour FROM Tour tour WHERE tour.status = 'ACCEPT' AND tour.isForSpecificTraveler = false AND tour.isDeleted = false ")
  Page<Tour> getListTours(Pageable pageable);
}
