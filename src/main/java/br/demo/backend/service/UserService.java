package br.demo.backend.service;


import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.properties.Property;
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
    private ProjectService projectService;

    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        for(User user : users){
            for(PermissionProject permissionProject : user.getProjects()){
                projectService.setProjectInPropertyOfProjectNull(permissionProject.getProject());
            }
        }
        return users;
    }

    public User findOne(Long id) {
        User user = userRepository.findById(id).get();
        for(PermissionProject permissionProject : user.getProjects()){
            projectService.setProjectInPropertyOfProjectNull(permissionProject.getProject());
        }
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
        User user = userRepository.findByUsernameAndPassword(username, password);
        for(PermissionProject permissionProject : user.getProjects()){
            projectService.setProjectInPropertyOfProjectNull(permissionProject.getProject());
        }
        return user;
    }

    public User findByEmailAndPassword(String mail, String password) {
        User user = userRepository.findByMailAndPassword(mail, password);
        for(PermissionProject permissionProject : user.getProjects()){
            projectService.setProjectInPropertyOfProjectNull(permissionProject.getProject());
        }
        return user;
    }

    public User findByUserNameOrName(String name) {
        User user = userRepository.findUserByUsernameOrName(name, name);
        for(PermissionProject permissionProject : user.getProjects()){
            projectService.setProjectInPropertyOfProjectNull(permissionProject.getProject());
        }
        return user;
    }
}
