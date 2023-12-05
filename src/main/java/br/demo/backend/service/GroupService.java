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
    private ProjectService projectService;

    public Collection<Group> findAll() {
        Collection<Group> groups = groupRepository.findAll();
        for(Group group : groups){
            ResolveStackOverflow.resolveStackOverflow(group);
        }
        return groups;
    }

    public Group findOne(Long id) {
        Group group = groupRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(group);
        return group;
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    public Collection<Group> findGroupsByUser(Long userId) {
        Collection<Group> groups =  groupRepository.findGroupsByUsersContaining(new User(userId));
        for(Group group : groups){
            ResolveStackOverflow.resolveStackOverflow(group);
        }
        return groups;
    }

    public Collection<Group> findGroupsOfAProject(Long projectId) {
        Collection<Group> groups =  groupRepository.findGroupsByProjects_Project(new Project(projectId));
        for(Group group : groups){
            ResolveStackOverflow.resolveStackOverflow(group);
        }
        return groups;
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
