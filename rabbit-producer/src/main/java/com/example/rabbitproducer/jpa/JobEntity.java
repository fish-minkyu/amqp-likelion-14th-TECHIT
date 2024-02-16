package com.example.rabbitproducer.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "job")
public class JobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String jobId;
  private String status;
  private String resultPath;
}
