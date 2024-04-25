package br.demo.backend.controller;

import br.demo.backend.model.Code;
import br.demo.backend.model.dtos.email.SendEmailDTO;
import br.demo.backend.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forgotPassword")
@AllArgsConstructor
public class EmailController {
    private EmailService emailService;

    @PostMapping
    public ResponseEntity sendEmail (@RequestBody SendEmailDTO sendEmailDTO){
        return ResponseEntity.ok(emailService.findUser(sendEmailDTO));
    }

    @GetMapping("/code")
    private ResponseEntity<List<Code>> getCode(){
        return ResponseEntity.ok(emailService.getCode());
    }
}
