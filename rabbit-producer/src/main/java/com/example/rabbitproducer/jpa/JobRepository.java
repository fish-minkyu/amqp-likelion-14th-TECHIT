package com.example.rabbitproducer.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
  Optional<JobEntity> findByJobId(String jobId);
}
