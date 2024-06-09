package com.example.localguidebe.service;

import com.example.localguidebe.dto.requestdto.AddTravelerRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateTravelerRequestDTO;
import com.example.localguidebe.entity.TravelerRequest;
import java.util.List;

public interface TravelerRequestService {
  TravelerRequest findTravelerRequestById(Long id);

  TravelerRequest addTravelerRequest(String email, AddTravelerRequestDTO addTravelerRequestDTO);

  List<TravelerRequest> getTravelerRequests(String email);

  TravelerRequest updateTravelerRequests(
      String email, UpdateTravelerRequestDTO updateTravelerRequestDTO, Long travelerRequestId);

  TravelerRequest updateTravelRequestStatus(
      String email, UpdateTravelerRequestDTO updateTravelerRequestDTO, Long travelerRequestId);

  void updateStatusAndTourForTravelerRequest(Long requestId, Long tourId);
}
