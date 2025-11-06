package com.resume.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.resume.repository.ResumeRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private ResumeService resumeService;

    @Test
    public void testAnalyzeResumeWithKeywords() {
        String content = "This is a resume with Java and Spring.";
        String keywords = "Java,Spring,React";

        String feedback = resumeService.analyzeResume(content, keywords);

        assertTrue(feedback.contains("Found keyword: Java"));
        assertTrue(feedback.contains("Found keyword: Spring"));
        assertTrue(feedback.contains("Missing keyword: React"));
    }
}
