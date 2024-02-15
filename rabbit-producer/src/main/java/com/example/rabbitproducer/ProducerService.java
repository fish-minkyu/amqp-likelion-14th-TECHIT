package com.example.rabbitproducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
  private final RabbitTemplate rabbitTemplate;
  private final Queue jobQueue;

  public void send(String message) {
    rabbitTemplate.convertAndSend(
      jobQueue.getName(), message
    );
    log.info("sent message: {}", message);
  }
}
