package com.example.rabbitpublisher;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Exchange 정의
public class PublisherConfig {
  // Fanout Exchange
  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("boot.fanout");
  }

  // Direct Exchange
  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange("boot.direct");
  }

  // Topic Exchange
  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange("boot.topic");
  }
}
