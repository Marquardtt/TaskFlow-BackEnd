package br.demo.backend.service;


import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.relations.PermissionProject;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupRepository groupRepository;
    private UserRepository userRepository;
    public Collection<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findOne(Long id) {
        return groupRepository.findById(id).get();
    }

    public void save(Group group, Long userId) {
        User user = userRepository.findById(userId).get();
        group.setOwner(user);
        groupRepository.save(group);
    }

    public Collection<Group> findGroupsByUser(Long userId) {
        return groupRepository.findGroupsByUsersContaining(new User(userId));
    }

    public Collection<Group> findGroupsOfAProject(Long projectId) {
        return groupRepository.findGroupsByProjects_Project(new Project(projectId));
    }

    public void update(Group group) {
        groupRepository.save(group);
    }
    public void updatePermission(Group group, Long projectId, Long permissionId) {
        for(PermissionProject permissionProject : group.getProjects()){
            if(permissionProject.getProject().getId().equals(projectId)){
                permissionProject.setPermission(new Permission(permissionId));
            }
        }
        for(User user : group.getUsers()){
            for(PermissionProject permissionProject : user.getProjects()){
                if(permissionProject.getProject().getId().equals(projectId)){
                    permissionProject.setPermission(new Permission(permissionId));
                }
            }
        }
        groupRepository.save(group);
    }


    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
