package com.example.rabbitproducer.dto;

import lombok.Data;

// 사용자가 처리를 요청하기 위한 DTO
@Data
public class JobRequest {
  private String filename;
}
