package com.example.localguidebe.service.impl;

import com.example.localguidebe.service.GeoCodingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

  private final RestTemplate restTemplate;

  public GeoCodingServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getAddress(double lat, double lng) {
    String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat={lat}&lon={lng}";

    String response = restTemplate.getForObject(url, String.class, lat, lng);

    return response;
  }
}
