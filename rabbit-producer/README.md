# RabbitMQ - Producer
- 2024.02.14

`2월 14일`
<details>
<summary><strong>Producer Page</strong></summary>

- RabbitMQ_Competing Consumers
<div>JobEntity</div>
<div>JobRepository</div>
<div>JobPayload: MQ에게 요청하는 작업 정보를 나타냄</div>
<div>JobRequest: 사용자가 MQ에게 처리를 요청</div>
<div>JobStatus: 사용자가 결과 처리 상태를 확인할 수 있음</div>
<div>ProducerConfig</div>
<div>ProducerService</div>
<div>ProducerController</div>
</details>

RabbitMQ를 활용해 메시지 브로커 코드를 구현했다.  
Competing Consumers이란 디자인 패턴의 일종을 구현해보았는데 Consumer Server가 많을 수록, 성능이 좋을 수록  
효과가 매우 뛰어나단 특징이 있다.  
메시지는 마치 Queue처럼 FIFO형식으로 작동을 한다.   
Consumer Server가 작업을 마치면 그 다음, 제일 앞에 있는 메시지를 처리하는 방식이다.  
즉, 이름과 같이 "경쟁하는 소비자들"로서 메시지를 빨리 처리하면 다음 메시지도 가져가도록 작동이 되는 디자인 패턴이다.

## 스팩

- Spring Boot 3.2.2
- Spring Web
- Spring Data Jpa
- Spring for RabbitMQ
- SQLite
- Gson
- Lombok

## Key Point

`02/14`

- [JobRequest](/src/main/java/com/example/rabbitproducer/dto/JobRequest.java): 작업을 생성하는 요청에 대한 DTO
```java
// 사용자가 처리를 요청하기 위한 DTO
@Data
public class JobRequest {
  private String filename;
}
```

- [JobPayload](/src/main/java/com/example/rabbitproducer/dto/JobPayload.java): 작업을 처리하는 서버에 데이터를 전달하기 위한 DTO
```java
// MQ를 통해 처리해야 하는 작업을 묘사하는 DTO
@Data
@AllArgsConstructor
public class JobPayload {
  // 개별적인 Job을 구분하고, 사용자가 요청 처리의 상태를 조회하기 위한 UUID
  private String jobId;
  private String filename; // 파일명
  private String path; // 파일 위치 경로
}
```

- [JobStatus](/src/main/java/com/example/rabbitproducer/dto/JobStatus.java): 생성된 작업을 추후 조회하기 위한 정보를 변환하는 DTO
```java
// 사용자에게 현재 처리 중인 작업의 처리상태를 공유하기 위한 DTO
@Data
@AllArgsConstructor
public class JobStatus {
  private String jobId;
  private String status; // 상태
  private String resultPath; // 결과 처리를 확인할 수 있는 경로

  public static JobStatus fromEntity(JobEntity entity) {
    return new JobStatus(
      entity.getJobId(),
      entity.getStatus(),
      entity.getResultPath()
    );
  }
}
```

- [ProducerConfig](/src/main/java/com/example/rabbitproducer/ProducerConfig.java): Queue 정의 후, Bean 객체로 등록
```java
@Configuration
public class ProducerConfig {
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
```

- [ProducerService](/src/main/java/com/example/rabbitproducer/ProducerService.java): Job을 생성하는 ProducerService
```java
@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
  private final RabbitTemplate rabbitTemplate;
  private final Queue jobQueue;
  private final JobRepository jobRepository;
  // Gson은 객체를 읽어들여서 Json으로 바꿔준다.
  private final Gson gson;

  public JobStatus send(JobRequest request) {
    // Consumer가 확인할 메시지를 만든다.
    String jobId = UUID.randomUUID().toString();
    String filename = request.getFilename();
    JobPayload payload = new JobPayload(
      jobId,
      filename,
      // 서버의 어디에 파일을 저장해두었는지
      String.format("/media/user-uploaded/raw/%s", filename)
    );
    // 전달한 정보를 데이터베이스에 기록
    JobEntity newJob = new JobEntity();
    newJob.setJobId(jobId);
    newJob.setStatus("WAIT");
    // resultPath는 Consumer가 기록해준다.
    JobStatus jobStatus = JobStatus.fromEntity(jobRepository.save(newJob));
    // 모든 정보가 준비되면 메시지를 브로커에 전송
    // gson.toJson(payload): JobPayload 객체를 문자열 형태로 직렬화해서 메시지 전송
    rabbitTemplate.convertAndSend(jobQueue.getName(), gson.toJson(payload));
    log.info("Sent Job: {}", jobId);
    // 사용자에게 응답
    return jobStatus;
  }

  // 사용자가 Producer에게 Ticket을 들고 Status를 확인하는 메서드
  public JobStatus getJobStatus(String jobId) {
    return JobStatus.fromEntity(jobRepository.findByJobId(jobId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }
}
```

