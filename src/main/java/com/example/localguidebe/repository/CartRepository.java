package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  @Query(
      "SELECT c FROM Cart c JOIN FETCH c.bookings b WHERE c.traveler.email = :email AND b.status = 'PENDING_PAYMENT'")
  Optional<Cart> getCartWithUnPaidBooKingByEmail(@Param("email") String email);

  Optional<Cart> getCartByTravelerEmail(String email);

  @Query(
      "SELECT  Count(cart) FROM Cart cart JOIN cart.bookings b WHERE cart.traveler.id = :travelerId AND b.tour.id = :tourId AND b.status ='PAID'")
  int getSuccessBookingNumber(@Param("tourId") Long tourId, @Param("travelerId") Long travelerId);
}
