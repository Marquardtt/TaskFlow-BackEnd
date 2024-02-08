package br.demo.backend.service;


import br.demo.backend.exception.GroupNotFoundException;
import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupRepository groupRepository;
    private ProjectService projectService;

    public Collection<Group> findAll() {
        Collection<Group> groups = groupRepository.findAll();
        return groups.stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Group findOne(Long id) {
        Group group = groupRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(group);
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    public Collection<Group> findGroupsByUser(Long userId) {
        Collection<Group> groups = groupRepository.findGroupsByUsersContaining(new User(userId));
        return groups.stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Collection<User> findUsersByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return group.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Permission findPermissionOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).get();
        return group.getPermission().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
    }

    public Collection<Group> findGroupsOfAProject(Long projectId) {
        Collection<Group> groups = groupRepository.findGroupsByPermission_Project(new Project(projectId));
        return groups.stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Collection<Permission> findAllPermissionsOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        return group.getPermission().stream()
                .filter(permission -> permission.getProject().getId().equals(projectId)).toList();
    }

    public void update(Group group) {
        Group groupOld = groupRepository.findById(group.getId()).get();
        Collection<Permission> permissions = groupOld.getPermission().stream().filter(p ->
                group.getPermission().stream().anyMatch(p1 ->
                        p1.getId().equals(p.getId()) && !p1.getPermission().equals(p.getPermission()))
        ).toList();
        for(Permission permission : permissions) {
            updatePermission(group, permission);
        }
        groupRepository.save(group);
    }


    public void updatePermission(Group group, Permission permission) {
        Collection <User> users = group.getUsers().stream().map( user -> {
            Collection<Permission> permissions = user.getPermission();
            permissions.removeAll(user.getPermission().stream().filter(p ->
                    p.getProject().getId().equals(permission.getId())).toList());
            permissions.add(permission);
            user.setPermission(permissions);
            return user;
        }).toList();
        group.setUsers(users);
    }

    public void updateUsers(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        group.getUsers().add(user);
        groupRepository.save(group);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }


    public void updateUserPermission(Long projectId, Long groupId, Long userId, Long permissionId) {
    }


}
