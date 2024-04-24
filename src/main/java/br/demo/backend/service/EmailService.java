package br.demo.backend.service;

import br.demo.backend.model.Code;
import br.demo.backend.model.dtos.email.SendEmailDTO;
import br.demo.backend.repository.CodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailService {

    private CodeRepository codeRepository;
    public ResponseEntity sendEmail(SendEmailDTO sendEmailDTO) {
        if (codeRepository.findAll() != null){
            codeRepository.deleteAll();
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(sendEmailDTO.getEmailFrom());
            message.setTo(sendEmailDTO.getEmailTo());
            message.setSubject("Redefinir senha!");
            String otp = generateOTP();

            String text = String.format("""
                Um pedido de mudança de senha foi pedido neste endereço de email
                
                Seu código de verificação é: %s
                
                Caso não tenha sido você, desconsidere esta mensagem.
                """, otp);

            message.setText(text);

        } catch (MailException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generateOTP(){
        int length = 4;
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        Code code = new Code();
        code.setCode(otp.toString());
        codeRepository.save(code);
        return otp.toString();
    }
}
