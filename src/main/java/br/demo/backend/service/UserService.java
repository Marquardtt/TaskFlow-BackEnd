package br.demo.backend.service;


import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.relations.PermissionProject;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.PermissionRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private PermissionRepository permissionRepository;

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(Long id) {
        User user = userRepository.findById(id).get();
        //Sorting by project date
        user.setProjects(
                user.getProjects().stream().sorted(
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
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User findByEmailAndPassword(String mail, String password) {
        return userRepository.findByMailAndPassword(mail, password);
    }

    public User findByUserNameOrName(String name) {
        return userRepository.findUserByUsernameOrName(name, name);
    }
}
