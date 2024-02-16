package com.example.rabbitsubscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriberService {
  // SpEL로 Queue를 지정한다.
  @RabbitListener(queues = "#{fanoutQueue.name}")
  public void receiveFanout(String message) {
    log.info("in fanout received: {}", message);
  }

  @RabbitListener(queues = "#{directQueue.name}")
  public void receiveDirect(String message) {
    log.info("in direct received: {}", message);
  }
}
