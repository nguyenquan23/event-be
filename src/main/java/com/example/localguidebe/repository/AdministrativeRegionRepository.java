package com.example.localguidebe.repository;

import com.example.localguidebe.entity.AdministrativeRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeRegionRepository extends JpaRepository<AdministrativeRegion,String> {

}
