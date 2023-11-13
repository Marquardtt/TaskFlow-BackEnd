package br.demo.backend.service;


import br.demo.backend.model.UserModel;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public Collection<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UserModel findOne(Long id) {
        return userRepository.findById(id).get();
    }

    public void save(UserModel userModel) {
        userRepository.save(userModel);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
