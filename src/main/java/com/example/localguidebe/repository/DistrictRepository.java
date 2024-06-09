package com.example.localguidebe.repository;

import com.example.localguidebe.entity.District;
import com.example.localguidebe.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,String> {
    @Query("SELECT d.nameEn FROM District d JOIN d.province p  WHERE p.nameEn LIKE %:provinceName% ")
    List<String> getDistrictByProvince(String provinceName);
}
