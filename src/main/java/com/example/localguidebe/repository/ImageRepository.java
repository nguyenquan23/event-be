package com.example.localguidebe.repository;


import com.example.localguidebe.entity.Image;
import com.example.localguidebe.enums.AssociateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("SELECT image FROM Image image WHERE image.associateName = :associateName AND image.associateId = :associateId ")
    List<Image> getImageByAssociateIddAndAssociateName(@Param("associateId") Long associateId,@Param("associateName") AssociateName associateName);

}
