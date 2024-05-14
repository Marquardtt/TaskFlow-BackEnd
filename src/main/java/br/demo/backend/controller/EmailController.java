package br.demo.backend.controller;

import br.demo.backend.model.Code;
import br.demo.backend.model.dtos.email.SendEmailDTO;
import br.demo.backend.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sendEmail")
@AllArgsConstructor
public class EmailController {
    private EmailService emailService;

    @PostMapping("/forgotPassword")
    public ResponseEntity sendEmailForgotPassword (@RequestBody SendEmailDTO sendEmailDTO){
        return ResponseEntity.ok(emailService.findUser(sendEmailDTO, "forgot-password"));
    }

    @PostMapping("/auth")
    public ResponseEntity sendEmailAuth (@RequestBody SendEmailDTO sendEmailDTO){
        return ResponseEntity.ok(emailService.findUser(sendEmailDTO, "auth"));
    }

    @GetMapping("/code")
    private ResponseEntity<List<Code>> getCode(){
        return ResponseEntity.ok(emailService.getCode());
    }
}
