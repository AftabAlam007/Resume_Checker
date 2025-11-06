package com.resume.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resume.service.ResumeService;
import com.resume.model.ResumeEntity;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeEntity> create(@RequestBody ResumeEntity resume) {
        return ResponseEntity.ok(resumeService.create(resume));
    }

    @GetMapping
    public ResponseEntity<List<ResumeEntity>> list() {
        return ResponseEntity.ok(resumeService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeEntity> get(@PathVariable Long id) {
        return ResponseEntity.of(resumeService.getById(id));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ResumeEntity> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("keywords") String keywords) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(resumeService.processResume(file, keywords));
    }

    @GetMapping("/results")
    public ResponseEntity<List<ResumeEntity>> getResults() {
        return ResponseEntity.ok(resumeService.getResults());
    }
}
