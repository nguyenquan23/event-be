package com.example.localguidebe.service;

import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.TourDupe;

public interface TourDupeService {
    TourDupe addTourDupe(Tour tour) throws Exception;
    TourDupe getTourDupe(Tour tour) throws Exception;
}
