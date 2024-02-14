package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private AutoMapper<User> autoMapper;

    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        return users.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public User findOne(String id) {
        User user = userRepository.findById(id).get();
        user.setPermissions(
                user.getPermissions().stream().sorted(
                        (p1, p2) -> p2.getProject().getVisualizedAt().compareTo(
                                p1.getProject().getVisualizedAt()
                        )).toList());
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }
    public void update(User userDTO, Boolean patching) {
        User user = patching ? userRepository.findById(userDTO.getUsername()).get() : new User();
        autoMapper.map(userDTO, user, patching);
        userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public Permission getPermissionOfAUserInAProject(String userId, Long projectId){
        User user = userRepository.findById(userId).get();
        return user.getPermissions().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
    }

    public User findByEmailAndPassword(String mail, String password) {
        User user = userRepository.findByMailAndPassword(mail, password);
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public void updatePicture(MultipartFile picture, String id) {
        User user = userRepository.findById(id).get();
        user.setPicture(new Archive(picture));
        userRepository.save(user);
    }


    public Collection<User> findByUserNameOrName(String name) {
        Collection<User> users = userRepository.findAllByUsernameContainingOrNameContaining(name, name);
        return users.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }
}
