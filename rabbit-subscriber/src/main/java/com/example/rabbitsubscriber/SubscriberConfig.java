package com.example.rabbitsubscriber;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriberConfig {
  @Bean
  // 내가 갈 우체국
  public FanoutExchange fanoutExchange() {
    // Publisher와 같은 우체국을 가야 데이터를 받아올 수 있다.
    return new FanoutExchange("boot.fanout");
  }

  @Bean
  // 내 우체통
  public Queue fanoutQueue() {
    // 다른 Queue와 겹치지 않을 우체동을 준비한다.
    return new AnonymousQueue();
  }

  @Bean
  public Binding fanoutBinding() {
    // 우체통을 우체국에 전달
    return BindingBuilder
      // @Bean 메서드를 호출해도 Bean 객체로 등록된다.
      .bind(fanoutQueue())
      .to(fanoutExchange());
  }

/*  @Bean
  // 우체통을 가지고 가서 우체국에 연결
  public Binding fanoutBinding(
    // 어떤 Queue로 등록된 Bean을 사용할지 결정
    // (여러 개의 Bean이 같은 이름으로 겹칠 때 사용할 수 있음)
    @Qualifier("fanoutQueue") Queue queue,
    FanoutExchange fanoutExchange
  ) {
    // 우체통을 우체국에 전달
    return BindingBuilder
      .bind(queue)
      .to(fanoutExchange);
  }*/

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange("boot.direct");
  }

  @Bean
  public Queue directQueue() {
    return new AnonymousQueue();
  }

  @Bean
  public Binding directBinding() {
    return BindingBuilder
      .bind(directQueue())
      .to(directExchange())
      // Binding key
      .with("warning");
  }
}
