# RabbitMQ - Publisher
- 2024.02.15

`2월 15일`
<details>
<summary><strong>Publish Page</strong></summary>

- Fanout Exchange & Direct Exchange & Topic Exchange

<div>PublisherConfig</div>
<div>PublisherService</div>
<div>PublisherController</div>
</details>

- PublisherConfig.java  
: Fanout Exchange & Direct Exchange & Topic Exchange들의 이름과 함께 정의를 선언한다.  


- PublisherService & PublisherController
: 각각의 Exchange별로 메시지를 보내는 핸들러를 작성해준다.

## 스팩

- Spring Boot 3.2.2
- Spring Web
- Lombok
- Spring for RabbitMQ

## Key Point

- Fanout Exchange & Direct Exchange & Topic Exchange 정의  
[PublisherConfig](/src/main/java/com/example/rabbitpublisher/PublisherConfig.java)
```java
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
```

- Fanout Exchange  
[PublisherService](/src/main/java/com/example/rabbitpublisher/PublisherService.java)  
```java
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
}
```
[PublisherController](/src/main/java/com/example/rabbitpublisher/PublisherController.java)
```java
@RestController
@RequiredArgsConstructor
public class PublisherController {
  private final PublisherService service;

  @PostMapping("/fanout")
  public void fanout(
    @RequestParam("message") String message
  ) {
    service.setFanoutMessage(message);
  }
}
```

- Direct Exchange  
  [PublisherService](/src/main/java/com/example/rabbitpublisher/PublisherService.java)
```java
@Service
@RequiredArgsConstructor
public class PublisherService {
  private final RabbitTemplate rabbitTemplate;
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
```

[PublisherController](/src/main/java/com/example/rabbitpublisher/PublisherController.java)
```java
@RestController
@RequiredArgsConstructor
public class PublisherController {
  private final PublisherService service;
  
  @PostMapping("/direct")
  public void direct(
    @RequestParam("key") String key,
    @RequestParam("message") String message
  ) {
    service.directMessage(key, message);
  }
}
```

- Topic Exchange  
  [PublisherService](/src/main/java/com/example/rabbitpublisher/PublisherService.java)
```java
public class PublisherService {
  private final RabbitTemplate rabbitTemplate;
  private final TopicExchange topicExchange;

  // topic 종류의 exchange에 message 보내기
  public void topicMessage(String topic, String message) {
    rabbitTemplate.convertAndSend(
      topicExchange.getName(),
      topic,
      message
    );
  }
}
```

[PublisherController](/src/main/java/com/example/rabbitpublisher/PublisherController.java)
```java
@RestController
@RequiredArgsConstructor
public class PublisherController {
  private final PublisherService service;

  @PostMapping("/topic")
  public void topic(
    @RequestParam("topic") String topic,
    @RequestParam("message") String message
  ) {
    service.topicMessage(topic, message);
  }
}
```