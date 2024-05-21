package br.demo.backend.service;

import br.demo.backend.model.Code;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.email.SendEmailDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.repository.CodeRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.utils.ModelToGetDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailService {

    private CodeRepository codeRepository;
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;
    private final String from = "gestaodeprojetosweg@gmail.com";

    public ResponseEntity sendEmailForgot(String username, String to) {
        codeRepository.deleteAll();
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setTo(to);
            message.setSubject("Redefinir senha!");
            String otp = generateOTP(username, to);

            String text = String.format("""
                Um pedido de mudança de senha foi pedido neste endereço de email
                
                Seu código de verificação é: %s
                
                Caso não tenha sido você, desconsidere esta mensagem.
                """, otp);

            message.setText(text);

             javaMailSender.send(message);

        } catch (MailException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity sendEmailAuth(String username, String to) {
        codeRepository.deleteAll();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("Autenticação de dois fatores!");
            String otp = generateOTPCode(username, to);

            String text = String.format("""
                Um pedido de login foi feito neste endereço de email
                
                Seu código de verificação é: %s
                
                Caso não tenha sido você, desconsidere esta mensagem.
                """, otp);

            message.setText(text);
                                        
           javaMailSender.send(message);


        } catch (MailException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String generateOTP(String username, String email){
        int length = 4;
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        Code code = new Code();
        code.setCode(otp.toString());
        code.setUsername(username);
        code.setEmail(email);
        codeRepository.save(code);
        return otp.toString();
    }


    public String generateOTPCode(String username, String email) {
        int length = 6;
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }

        Code code = new Code();
        code.setCode(otp.toString());
        code.setUsername(username);
        code.setEmail(email);
        codeRepository.save(code);

        return otp.toString();
    }

    public List<Code> getCode() {
        return codeRepository.findAll();
    }

    public ResponseEntity findUser(SendEmailDTO sendEmailDTO, String method) {
        User user = userRepository.findByUserDetailsEntity_Username(sendEmailDTO.getUsername()).get();
        UserGetDTO userGetDTO = ModelToGetDTO.tranform(user);
        if (method.equals("forgot-password")){
            sendEmailForgot(sendEmailDTO.getUsername(), userGetDTO.getMail());
        } else{
            sendEmailAuth(sendEmailDTO.getUsername(), userGetDTO.getMail());
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
