# RabbitMQ - Consumer
`2월 14일`
<details>
<summary><strong>Consumer Page</strong></summary>

- RabbitMQ_Competing Consumers
<div>JobPayload: MQ에게 요청하는 작업 정보를 나타냄</div>
<div>ConsumerConfig</div>
<div>ConsumerService</div>
</details>

RabbitMQ를 활용해 메시지 브로커 코드를 구현했다.  
Competing Consumers이란 디자인 패턴의 일종을 구현해보았는데 Consumer Server가 많을 수록, 성능이 좋을 수록  
효과가 매우 뛰어나단 특징이 있다.  
메시지는 마치 Queue처럼 FIFO형식으로 작동을 한다.   
Consumer Server가 작업을 마치면 그 다음, 제일 앞에 있는 메시지를 처리하는 방식이다.  
즉, 이름과 같이 "경쟁하는 소비자들"로서 메시지를 빨리 처리하면 다음 메시지도 가져가도록 작동이 되는 디자인 패턴이다.


## 스팩

- Spring Boot 3.2.2
- Spring Data Jpa
- Spring for RabbitMQ
- SQLite
- Gson
- Lombok

## Key Point

- [application.yqml](/src/main/resources/application.yaml)


: JobEntity & JobRepository는 Producer와 DB를 공유하기에 같아야 한다.  
따라서, SQLite를 공유하며 `ddl-auto`는 `none`으로 해줘야 한다.

```yaml
spring:
  rabbitmq:
    # ...

  datasource:
    # 상위 경로에 있는 producer의 db sqlite를 사용하겠다란 의미
    url: jdbc:sqlite:../rabbit-producer/db.sqlite
    driver-class-name: org.sqlite.JDBC
    username: sa
    password: password

  jpa:
    hibernate:
      # producer의 DB를 사용하므로 none이다.
      ddl-auto: none
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    show-sql: true
```

- [JobPayload](/src/main/java/com/example/rabbitconsumer/dto/JobPayload.java): JobPayload 또한, Consumer에서 Producer가 보낸 dto를 그대로 받기 때문에 같아야 한다.



- [ConsumerConfig](/src/main/java/com/example/rabbitconsumer/ConsumerConfig.java): Producer와 똑같이 설정해주면 된다.


- [ConsumerService](/src/main/java/com/example/rabbitconsumer/ConsumerService.java)
```java
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
  private final JobRepository jobRepository;
  private final Gson gson;
  // @RabbitListener
  // : RabbitMQ의 요청을 들을 것이다.
  @RabbitListener(queues = "boot.amqp.job-queue") // queues = "#{queue.name}"
  public void  receive(String message) throws InterruptedException{
    log.info("received: {}", message);
    // 처리해야할 작업을 받는다.
    JobPayload jobPayload = gson.fromJson(message, JobPayload.class);
    String jobId = jobPayload.getJobId();
    log.info("job id: {}", jobId);
    // DB에서 처리할 작업의 상세정보를 받아왔다.
    JobEntity jobEntity
      = jobRepository.findByJobId(jobId)
      // 잘못된 정보가 포함된 요청은 다시 처리하지 않도록 (Amqp를 사용할 때 쓸 수 있는 예외 객체)
      .orElseThrow(() -> new AmqpRejectAndDontRequeueException(jobId));

    // 처리 되기 전에 "처리 중"이라고 상태를 업데이트 한다.
    jobEntity.setStatus("PROCESSING");
    jobEntity = jobRepository.save(jobEntity);
    log.info("Start processing: {}", jobId);

    // 6초 대기
    Thread.sleep(6000);

    // 작업이 완료되었다.
    jobEntity.setStatus("DONE");
    jobEntity.setResultPath(
      // 완료 경로 설정(중요한 것은 아님, 경로는 임의 설정함)
      String.format("/media/user-uploaded/processed/%s", jobPayload.getFilename())
    );
    jobRepository.save(jobEntity);
    log.info("completed: {}", jobId);
    // => Producer와 DB를 공유하고 있으므로 사용자가 Ticket을 들고 상태를 확인하면 "작업 완료"를 확인할 수 있다.
  }
}
```