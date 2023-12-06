package br.demo.backend.service;


import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        for(User user : users){
            ResolveStackOverflow.resolveStackOverflow(user);
        }
        return users;
    }

    public User findOne(Long id) {
        User user = userRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(user);
        user.setPermission(
                user.getPermission().stream().sorted(
                        (p1, p2) -> p2.getProject().getVisualizedAt().compareTo(
                                p1.getProject().getVisualizedAt()
                        )).collect(Collectors.toList()));
        return user;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        ResolveStackOverflow.resolveStackOverflow(user);
        return user;
    }

    public Permission getPermissionOfAUserInAProject(Long userId, Long projectId){
        User user = userRepository.findById(userId).get();
        Permission permission = user.getPermission().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
        return permission;
    }

    public User findByEmailAndPassword(String mail, String password) {
        User user = userRepository.findByMailAndPassword(mail, password);
        ResolveStackOverflow.resolveStackOverflow(user);
        return user;
    }

    public User findByUserNameOrName(String name) {
        User user = userRepository.findUserByUsernameOrName(name, name);
        ResolveStackOverflow.resolveStackOverflow(user);
        return user;
    }
}
