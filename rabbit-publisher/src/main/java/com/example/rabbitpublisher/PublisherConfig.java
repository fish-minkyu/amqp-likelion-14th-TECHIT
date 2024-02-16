package com.example.rabbitpublisher;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Exchange 정의
public class PublisherConfig {
  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("boot.fanout");
  }
}
