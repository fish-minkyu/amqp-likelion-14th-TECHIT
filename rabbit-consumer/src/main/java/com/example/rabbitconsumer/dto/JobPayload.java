package com.example.rabbitconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// MQ를 통해 처리해야 하는 작업을 묘사하는 DTO
@Data
@AllArgsConstructor
public class JobPayload {
  // 개별적인 Job을 구분하고, 사용자가 요청 처리의 상태를 조회하기 위한 UUID
  private String jobId;
  private String filename; // 파일명
  private String path; // 파일 위치 경로
}
