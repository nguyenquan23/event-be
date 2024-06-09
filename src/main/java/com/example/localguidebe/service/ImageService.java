package com.example.localguidebe.service;

import com.example.localguidebe.entity.Image;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.enums.FolderName;
import java.util.List;

public interface ImageService {
  void addImage(
      String imageInBase64, Long associateId, AssociateName associateName, FolderName folderName);

  List<Image> getImageByAssociateIddAndAssociateName(Long associateId, AssociateName associateName);
}
