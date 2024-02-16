package com.example.rabbitconsumer;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {
  // 메시지 큐 정의
  @Bean
  public Queue queue() {
    // durable: 서버가 꺼졌다 켜져도 메시지 큐가 유지 될지 결정
    // exclusive: 이 서버에서 사용하는 Queue인지에 대한 설정
    // autoDelete: 해당 큐를 사용하지 않을 때 삭제 유무 결정
    return new Queue(
      "boot.amqp.job-queue",
      true,
      false,
      true
    );
  }
}
