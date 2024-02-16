package com.example.rabbitconsumer;

import com.example.rabbitconsumer.dto.JobPayload;
import com.example.rabbitconsumer.jpa.JobEntity;
import com.example.rabbitconsumer.jpa.JobRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

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
