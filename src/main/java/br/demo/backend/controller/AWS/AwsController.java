package br.demo.backend.controller.AWS;

import br.demo.backend.service.AwsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/aws")
public class AwsController {

    private AwsService service;
    @PostMapping("/{id}")
    public void postAws(@PathVariable Long id , @RequestParam MultipartFile file){
        try {
            service.uploadFile(file, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
