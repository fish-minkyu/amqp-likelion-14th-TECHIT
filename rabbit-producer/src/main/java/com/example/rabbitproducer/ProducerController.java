package com.example.rabbitproducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {
  private final ProducerService service;

  @PostMapping("/make-job")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void makeJob(
    @RequestParam("message") String message
  ) {
    service.send(message);
  }
}
