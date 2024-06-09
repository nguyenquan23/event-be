package com.example.localguidebe.controller;

import com.example.localguidebe.system.Result;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
public class TestWS {
  @Autowired SimpMessagingTemplate messagingTemplate;

  @PostMapping("/addOfflineOrder")
  public ResponseEntity<Result> addOfflineOrder() {
    messagingTemplate.convertAndSend("/topic/notify", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.OK)
        .body(new Result(true, HttpStatus.OK.value(), "All guide"));
  }
}
