package br.demo.backend.security.service;

import br.demo.backend.model.Code;
import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TwoFactorAuthenticateService {

    UserRepository userRepository;
    EmailService emailService;


    public boolean verifyTwoFactor(Code responseUser){
        List<Code> codes = emailService.getCode();
        return codes.contains(responseUser);

    }

}
