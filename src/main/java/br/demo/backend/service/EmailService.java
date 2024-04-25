package br.demo.backend.service;

import br.demo.backend.model.Code;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.email.SendEmailDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.repository.CodeRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.utils.ModelToGetDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailService {

    private CodeRepository codeRepository;
    private UserRepository userRepository;
    public ResponseEntity sendEmail(String to, String from) {
        codeRepository.deleteAll();
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setTo(to);
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

    public List<Code> getCode() {
        return codeRepository.findAll();
    }

    public ResponseEntity findUser(SendEmailDTO sendEmailDTO) {
        User user = userRepository.findByUserDetailsEntity_Username(sendEmailDTO.getUsername()).get();
        UserGetDTO userGetDTO = ModelToGetDTO.tranform(user);
        sendEmail(userGetDTO.getMail(), sendEmailDTO.getEmailFrom());
        return new ResponseEntity(HttpStatus.OK);
    }
}
