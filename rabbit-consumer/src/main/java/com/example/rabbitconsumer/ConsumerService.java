package com.example.rabbitconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
  // @RabbitListener
  // : RabbitMQ의 요청을 들을 것이다.
  @RabbitListener(queues = "boot.amqp.job-queue") // queues = "#{queue.name}"
  public void  receive(String message) throws InterruptedException{
    log.info("received: {}", message);
    // 3초 대기
    Thread.sleep(3000);
    log.info("completed: {}", message);

  }

}
