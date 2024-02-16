package com.example.rabbitpublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublisherController {
  private final PublisherService service;

  @PostMapping("/fanout")
  public void fanout(
    @RequestParam("message") String message
  ) {
    service.setFanoutMessage(message);
  }

  @PostMapping("/direct")
  public void direct(
    @RequestParam("key") String key,
    @RequestParam("message") String message
  ) {
    service.directMessage(key, message);
  }
}
