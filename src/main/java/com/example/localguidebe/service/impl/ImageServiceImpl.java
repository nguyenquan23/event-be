package com.example.localguidebe.service.impl;

import com.example.localguidebe.entity.Image;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.enums.FolderName;
import com.example.localguidebe.repository.ImageRepository;
import com.example.localguidebe.service.ImageService;
import com.example.localguidebe.utils.CloudinaryUtil;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final CloudinaryUtil cloudinaryUtil;

  @Autowired
  public ImageServiceImpl(ImageRepository imageRepository, CloudinaryUtil cloudinaryUtil) {
    this.imageRepository = imageRepository;
    this.cloudinaryUtil = cloudinaryUtil;
  }

  @Override
  public void addImage(
      String imageInBase64, Long associateId, AssociateName associateName, FolderName folderName) {
    Image image = new Image();
    try {
      image.setImageLink(cloudinaryUtil.uploadFile(imageInBase64, folderName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    image.setAssociateId(associateId);
    image.setAssociateName(associateName);
    imageRepository.save(image);
  }

  public List<Image> getImageByAssociateIddAndAssociateName(
      Long associateId, AssociateName associateName) {

    return imageRepository.getImageByAssociateIddAndAssociateName(associateId, associateName);
  }
}
