package com.example.localguidebe.repository;

import com.example.localguidebe.entity.TravelerRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelerRequestRepository extends JpaRepository<TravelerRequest, Long> {
  @Query(
      "SELECT tr FROM TravelerRequest tr JOIN tr.traveler t JOIN tr.guide g WHERE t.email = :email OR g.email = :email")
  List<TravelerRequest> getTravelerRequests(@Param("email") String email);

  @Modifying
  @Query("delete from TravelerRequest tl where tl.id =:id")
  void deleteById(@Param("id") Long id);
}
