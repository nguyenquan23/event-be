package com.example.localguidebe.converter;

import com.example.localguidebe.dto.ImageDTO;
import com.example.localguidebe.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageToImageDtoConverter {
    public ImageDTO convert(Image source) {
        return new ImageDTO(source.getId(), source.getImageLink());
    }
}
