package br.demo.backend.controller;

import br.demo.backend.service.AwsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/aws")
@AllArgsConstructor
public class AwsController {
    private final AwsService awsService;

    @PostMapping("/{id}")
    public void postAwsA3(@PathVariable Long id, @RequestBody MultipartFile file){
        awsService.update(id, file);

    }
}
