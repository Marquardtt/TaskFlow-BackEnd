package br.demo.backend.service;


import br.demo.backend.exception.GroupNotFoundException;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.*;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupRepository groupRepository;
    private ProjectService projectService;
    private UserRepository userRepository;
    private AutoMapper<Group> autoMapper;


    public Collection<Group> findAll() {
        Collection<Group> groups = groupRepository.findAll();
        return groups.stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Group findOne(Long id) {
        Group group = groupRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(group);
    }

    public void save(Group group) {
        if(group.getPermissions() != null){
            updatePermission(group, group.getPermissions().stream().findFirst().get());
        }
        groupRepository.save(group);
    }

    public void updateOwner(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        group.setOwner(user);
        groupRepository.save(group);
    }
    public Collection<Group> findGroupsByUser(String userId) {
        Collection<Group> groups = groupRepository.findGroupsByUsersContaining(new User(userId));
        return groups.stream().distinct().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Collection<User> findUsersByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return group.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Permission findPermissionOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).get();
        Permission permission = group.getPermissions().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
        permission.setProject(ResolveStackOverflow.resolveStackOverflow(permission.getProject()));
        return permission;
    }

    public Collection<Group> findGroupsOfAProject(Long projectId) {
        Collection<Group> groups = groupRepository.findGroupsByPermissions_Project(new Project(projectId));
        return groups.stream().map(ResolveStackOverflow::resolveStackOverflow).collect(Collectors.toList());
    }

    public Collection<Permission> findAllPermissionsOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        return group.getPermissions().stream()
                .filter(permission -> permission.getProject().getId().equals(projectId)).toList();
    }

    public void update(Group groupDTO, Boolean patching) {

        Group group = patching ? groupRepository.findById(groupDTO.getId()).get() : new Group();
        User owner = group.getOwner();
        autoMapper.map(groupDTO, group, patching);

        Group groupOld = groupRepository.findById(group.getId()).get();
        Collection<Permission> permissions = group.getPermissions().stream().filter(p ->
                !groupOld.getPermissions().contains(p)
        ).toList();
        for(Permission permission : permissions) {
            updatePermission(group, permission);
        }
        group.setOwner(owner);
        groupRepository.save(group);
    }

    private User updatePermissionInAUser(User user, Permission permission){
        Collection<Permission> permissions = user.getPermissions();
        if(user.getPermissions() != null) {
            permissions.removeAll(user.getPermissions().stream().filter(p ->
                    p.getProject().getId().equals(permission.getProject().getId())).toList());
        }else{
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        user.setPermissions(permissions);
        return user;
    }


    public void updatePermission(Group group, Permission permission) {
        Collection <User> users = group.getUsers().stream().map( u -> {
            User user = updatePermissionInAUser(u, permission);
            userRepository.save(user);
            return user;
        }).toList();
        group.setUsers(users);
        User owner = updatePermissionInAUser(group.getOwner(), permission);
        userRepository.save(owner);
        group.setOwner(owner);
    }

    public void updateUsers(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        group.getUsers().add(user);
        groupRepository.save(group);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    public void updatePicture(MultipartFile picture, Long id) {
        Group group = groupRepository.findById(id).get();
        group.setPicture(new Archive(picture));
        groupRepository.save(group);
    }

    public void updateUserPermission(Long projectId, Long groupId, Long userId, Long permissionId) {
    }


}
