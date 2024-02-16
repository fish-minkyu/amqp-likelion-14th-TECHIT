package com.example.rabbitproducer;

import com.example.rabbitproducer.dto.JobRequest;
import com.example.rabbitproducer.dto.JobStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProducerController {
  private final ProducerService service;

//  @PostMapping("/make-job")
//  @ResponseStatus(HttpStatus.NO_CONTENT)
//  public void makeJob(
//    @RequestParam("message") String message
//  ) {
//    service.send(message);
//  }

  @PostMapping("/make-job")
  public JobStatus makeJob(
    @RequestBody JobRequest dto
    ) {
    return service.send(dto);
  }
}
