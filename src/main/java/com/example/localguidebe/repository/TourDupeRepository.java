package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.TourDupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDupeRepository extends JpaRepository<TourDupe, Long> {}
