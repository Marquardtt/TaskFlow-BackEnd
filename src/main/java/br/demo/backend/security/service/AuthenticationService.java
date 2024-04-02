package br.demo.backend.security.service;

import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUserDetailsEntity_Username(username);
        if (userOptional.isPresent()){
            return userOptional.get().getUserDetailsEntity();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
