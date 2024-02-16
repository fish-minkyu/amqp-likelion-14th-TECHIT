package com.example.rabbitpublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {
  private final RabbitTemplate rabbitTemplate;
  private final FanoutExchange fanoutExchange;

  // fanout 종류의 exchange에 message 보내기
  public void setFanoutMessage(String message) {
    rabbitTemplate.convertAndSend(
      // fanoutExchange의 이름을 가진 Exchange에 데이터를 보내겠다.
      fanoutExchange.getName(),
      "",
      // 해당 메시지를 보내겠다.
      message
    );
  }

  private final DirectExchange directExchange;

  // direct 종류의 exchange에 message 보내기
  public void directMessage(String key, String message) {
    rabbitTemplate.convertAndSend(
      directExchange.getName(),
      // Routing Key
      key,
      message
    );
  }
}
