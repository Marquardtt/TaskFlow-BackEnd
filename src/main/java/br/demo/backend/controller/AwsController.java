package br.demo.backend.controller;

import br.demo.backend.service.AwsService;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.Environment;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/aws")
public class AwsController {


    private final AwsService awsService;
    @PostMapping("/{id}")
    public void postAwsA3(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        awsService.uploadFile(id, file);
    }

    @GetMapping("/{id}")
    public String getAws3(@PathVariable Long id){
        return awsService.getAws3(id);
    }
}