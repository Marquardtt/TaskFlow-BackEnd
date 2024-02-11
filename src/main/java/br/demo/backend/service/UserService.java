package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public User findOne(Long id) {
        User user = userRepository.findById(id).get();
        user.setPermission(
                user.getPermission().stream().sorted(
                        (p1, p2) -> p2.getProject().getVisualizedAt().compareTo(
                                p1.getProject().getVisualizedAt()
                        )).toList());
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }
    public void update(User userDTO, Boolean patching) {
        User user = patching ? userRepository.findById(userDTO.getId()).get() : new User();
        autoMapper.map(userDTO, user, patching);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public Permission getPermissionOfAUserInAProject(Long userId, Long projectId){
        User user = userRepository.findById(userId).get();
        return user.getPermission().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
    }

    public User findByEmailAndPassword(String mail, String password) {
        User user = userRepository.findByMailAndPassword(mail, password);
        return ResolveStackOverflow.resolveStackOverflow(user);
    }

    public User findByUserNameOrName(String name) {
        User user = userRepository.findUserByUsernameOrName(name, name);
        return ResolveStackOverflow.resolveStackOverflow(user);
    }
}
