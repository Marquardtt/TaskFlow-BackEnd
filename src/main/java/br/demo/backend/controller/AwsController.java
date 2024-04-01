package br.demo.backend.controller;

import br.demo.backend.service.AwsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aws")
public class AwsController {


    private final AwsService awsService;
    @PostMapping("/{id}")
    public void post(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        awsService.uploadFile(id, file);
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id) {
        return awsService.getImage(id);
    }
}
