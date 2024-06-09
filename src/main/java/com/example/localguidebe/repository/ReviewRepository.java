package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query(
      "SELECT r FROM User u JOIN u.reviewsOfGuide r WHERE u.id = :guideId AND r.rating IN :ratings ORDER BY CASE WHEN :sortBy = 'Most recent' THEN r.createdAt END DESC ,CASE WHEN :sortBy ='Highest rated' THEN r.rating END DESC ,CASE WHEN :sortBy = 'Lowest rated' THEN r.rating END ASC ")
  List<Review> getReviewsByGuide(
      @Param("guideId") Long guideId,
      @Param("ratings") List<Integer> ratings,
      @Param("sortBy") String sortBy);

  @Modifying
  @Query("delete from Review r where r.id = :id")
  void deleteById(@Param("id") Long id);

  @Query("SELECT COUNT(r) FROM Review r WHERE r.traveler.id = :travelerId AND r.tour.id = :tourId ")
  int getReviewNumber(@Param("travelerId") Long travelerId, @Param("tourId") Long tourId);
}
