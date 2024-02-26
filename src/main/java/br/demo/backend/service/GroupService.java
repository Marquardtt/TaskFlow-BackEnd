package br.demo.backend.service;


import br.demo.backend.exception.GroupNotFoundException;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ModelToGetDTO;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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


    public Collection<GroupGetDTO> findAll() {
        return groupRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public GroupGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(groupRepository.findById(id).get());
    }

    public void save(GroupPostDTO groupDto) {
        Group group = new Group();
        BeanUtils.copyProperties(groupDto, group);
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
    public Collection<GroupGetDTO> findGroupsByUser(String userId) {
        return groupRepository.findGroupsByUsersContaining(new User(userId)).stream().map(ModelToGetDTO::tranform).toList();
    }

    public PermissionGetDTO findPermissionOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).get();
        Permission permission = group.getPermissions().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
        return ModelToGetDTO.tranform(permission);
    }


    public Collection<Permission> findAllPermissionsOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        return group.getPermissions().stream()
                .filter(permission -> permission.getProject().getId().equals(projectId)).toList();
    }

    public void update(GroupPutDTO groupDTO, Boolean patching) {
        Group oldGroup = groupRepository.findById(groupDTO.getId()).get();
        Archive picture = oldGroup.getPicture();

        Group group = patching ? oldGroup : new Group();
        User owner = oldGroup.getOwner();
        autoMapper.map(groupDTO, group, patching);

        Group groupOld = groupRepository.findById(group.getId()).get();
        Collection<Permission> permissions = group.getPermissions().stream().filter(p ->
                !groupOld.getPermissions().contains(p)
        ).toList();
        for(Permission permission : permissions) {
            updatePermission(group, permission);
        }
        group.setOwner(owner);
        group.setPicture(picture);
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
            User user = updatePermissionInAUser((userRepository.findById(u.getUsername()).get()), permission);
            System.out.println(user.getPassword());
            userRepository.save(user);
            return user;
        }).toList();
        group.setUsers(users);
        User owner = updatePermissionInAUser(userRepository.findById(group.getOwner().getUsername()).get(), permission);
        userRepository.save(owner);
        group.setOwner(owner);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    public void updatePicture(MultipartFile picture, Long id) {
        Group group = groupRepository.findById(id).get();
        group.setPicture(new Archive(picture));
        groupRepository.save(group);
    }

}
