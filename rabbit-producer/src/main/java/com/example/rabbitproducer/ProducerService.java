package com.example.rabbitproducer;

import com.example.rabbitproducer.dto.JobPayload;
import com.example.rabbitproducer.dto.JobRequest;
import com.example.rabbitproducer.dto.JobStatus;
import com.example.rabbitproducer.jpa.JobEntity;
import com.example.rabbitproducer.jpa.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
  private final RabbitTemplate rabbitTemplate;
  private final Queue jobQueue;
  private final JobRepository jobRepository;
  // Gson은 객체를 읽어들여서 Json으로 바꿔준다.
  private final Gson gson;

  public void send(String message) {
    rabbitTemplate.convertAndSend(
      jobQueue.getName(), message
    );
    log.info("sent message: {}", message);
  }

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
