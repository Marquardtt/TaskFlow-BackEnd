package br.demo.backend.controller;


import br.demo.backend.service.AWSService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/aws")
public class AWSController {

    private AWSService awsService;

    @PostMapping("/{id}")
    public void postFile (@PathVariable Long id, @RequestParam MultipartFile file){
        awsService.insertImage(id, file);
    }

    @GetMapping("/{id}")
    public void getFile (@PathVariable Long id) {
    }


}
