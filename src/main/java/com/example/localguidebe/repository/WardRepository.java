package com.example.localguidebe.repository;

import com.example.localguidebe.entity.District;
import com.example.localguidebe.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,String> {

    @Query("SELECT w.nameEn FROM Ward w JOIN w.district d WHERE d.nameEn LIKE %:districtName% ")
    List<String> getWardByDistrict(String districtName);




}
