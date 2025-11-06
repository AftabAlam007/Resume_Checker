package com.resume.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resume.model.ResumeEntity;
import com.resume.repository.ResumeRepository;
import com.resume.util.PdfTextExtractor;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeEntity create(ResumeEntity resume) {
        return resumeRepository.save(resume);
    }

    public List<ResumeEntity> listAll() {
        return resumeRepository.findAll();
    }

    public Optional<ResumeEntity> getById(Long id) {
        return resumeRepository.findById(id);
    }

        public ResumeEntity processResume(MultipartFile file, String keywords) {

            if (file == null || file.isEmpty()) {

                throw new IllegalArgumentException("File cannot be empty");

            }

    

            if (!isPdfFile(file)) {

                throw new IllegalArgumentException("Only PDF files are allowed");

            }

    

            try (InputStream inputStream = file.getInputStream()) {

                ResumeEntity resume = new ResumeEntity();

                resume.setFileName(file.getOriginalFilename());

                resume.setContent(PdfTextExtractor.extractText(inputStream));

                resume.setStatus("PROCESSING");

                resume.setUploadedAt(new java.util.Date());

    

                // Process and analyze the resume content

                String feedback = analyzeResume(resume.getContent(), keywords);

                resume.setFeedback(feedback);

                resume.setStatus("COMPLETED");

    

                return resumeRepository.save(resume);

            } catch (IOException e) {

                throw new RuntimeException("Failed to process resume file", e);

            }

        }

    

        private boolean isPdfFile(MultipartFile file) {

            String contentType = file.getContentType();

            return contentType != null && contentType.equals("application/pdf");

        }

    

            public String analyzeResume(String content, String keywords) {
        
                try {
        
                    return analyzeResumeWithOpenNLP(content, keywords);
        
                } catch (IOException e) {
        
                    throw new RuntimeException("Failed to analyze resume with OpenNLP", e);
        
                }
        
            }
        
        
        
            public String analyzeResumeWithOpenNLP(String content, String keywords) throws IOException {
        
                // TODO: Download the OpenNLP models and place them in the resources directory
        
                // en-sent.bin: https://opennlp.sourceforge.net/models-1.5/
        
                // en-token.bin: https://opennlp.sourceforge.net/models-1.5/
    

            StringBuilder feedback = new StringBuilder();

    

            try (InputStream sentModelIn = getClass().getResourceAsStream("/models/en-sent.bin");

                 InputStream tokenModelIn = getClass().getResourceAsStream("/models/en-token.bin")) {

    

                if (sentModelIn == null || tokenModelIn == null) {

                    throw new IOException("OpenNLP models not found in resources");

                }

    

                SentenceModel sentModel = new SentenceModel(sentModelIn);

                SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentModel);

    

                TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);

                TokenizerME tokenizer = new TokenizerME(tokenModel);

    

                // Basic content checks

                if (content == null || content.trim().isEmpty()) {

                    feedback.append("- Resume content is empty.\n");

                } else {

                    // Check for contact information

                    if (!content.matches(".*\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b.*")) {

                        feedback.append("- Missing email address.\n");

                    }

                    if (!content.matches(".*\\b\\(?\\d{3}\\)?[ -]?\\d{3}[ -]?\\d{4}\\b.*")) {

                        feedback.append("- Missing phone number.\n");

                    }

    

                    // Keyword analysis

                    if (keywords != null && !keywords.trim().isEmpty()) {

                        String[] keywordArr = keywords.split(",");

                        for (String keyword : keywordArr) {

                            if (content.toLowerCase().contains(keyword.trim().toLowerCase())) {

                                feedback.append("- Found keyword: ").append(keyword.trim()).append("\n");

                            } else {

                                feedback.append("- Missing keyword: ").append(keyword.trim()).append("\n");

                            }

                        }

                    }

    

                    // Advanced analysis using OpenNLP

                    String[] sentences = sentenceDetector.sentDetect(content);

                    long quantifiableAchievementCount = 0;
                    long actionVerbCount = 0;
                    List<String> actionVerbs = List.of("achieved", "managed", "led", "developed", "created", "implemented", "increased", "decreased", "improved", "negotiated");

                    for (String currentSentence : sentences) {
                        // Quantifiable Achievement Detection
                        if (currentSentence.matches(".*\\d+.*") || currentSentence.matches(".*[%$€£].*")) {
                            quantifiableAchievementCount++;
                        }

                        // Action Verb Detection
                        String[] currentTokens = tokenizer.tokenize(currentSentence.toLowerCase());
                        for (String token : currentTokens) {
                            if (actionVerbs.contains(token)) {
                                actionVerbCount++;
                            }
                        }
                    }
                    feedback.append("- Found ").append(actionVerbCount).append(" action verbs.\n");
                    feedback.append("- Found ").append(quantifiableAchievementCount).append(" quantifiable achievements.\n");

                    // Resume Length Check
                    int wordCount = content.split("\\s+").length;
                    feedback.append("- Resume length: ").append(wordCount).append(" words.\n");
                    if (wordCount < 400 || wordCount > 600) {
                        feedback.append("- Ideal resume length is between 400 and 600 words.\n");
                    }

                    // TODO: Implement spelling and grammar check

    

                    // Check for basic sections

                    if (!content.toLowerCase().contains("experience")) {

                        feedback.append("- Missing work experience section.\n");

                    }

                    if (!content.toLowerCase().contains("education")) {

                        feedback.append("- Missing education section.\n");

                    }

                    if (!content.toLowerCase().contains("skills")) {

                        feedback.append("- Missing skills section.\n");

                    }

                }

            }

    

            return feedback.length() > 0 ? feedback.toString() : "Resume looks good!";

        }

    public List<ResumeEntity> getResults() {
        return resumeRepository.findAll();
    }
}
