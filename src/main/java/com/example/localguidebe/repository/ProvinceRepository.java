package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,String> {
    @Query("SELECT p.nameEn FROM Province p")
    List<String> getProvinceByName();

}
