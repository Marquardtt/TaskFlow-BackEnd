package br.demo.backend.service;


import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(Long id) {
        return userRepository.findById(id).get();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    public User findByEmailAndPassword(String mail, String password) {
        return userRepository.findByMailAndPassword(mail, password);
    }

    public User findByName(String name) {
        return userRepository.findByNameContains(name);
    }
}
