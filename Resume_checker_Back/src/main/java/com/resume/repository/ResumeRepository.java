package com.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resume.model.ResumeEntity;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
}
