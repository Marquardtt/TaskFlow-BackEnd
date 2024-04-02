package br.demo.backend.service;




import br.demo.backend.exception.GroupNotFoundException;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class GroupService {


    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;;
    private AutoMapper<Group> autoMapper;


    public Collection<GroupGetDTO> findAll() {
        return groupRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }


    public GroupGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(groupRepository.findById(id).get());
    }


    public GroupGetDTO save(GroupPostDTO groupDto) {
        Group group = new Group();
        BeanUtils.copyProperties(groupDto, group);
        if(group.getPermissions() != null){
            updatePermission(group, group.getPermissions().stream().findFirst().get());
        }
        return ModelToGetDTO.tranform(groupRepository.save(group));

    }

    public GroupGetDTO updateOwner(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        group.setOwner(user);
        return ModelToGetDTO.tranform(groupRepository.save(group));

    }
    public Collection<GroupGetDTO> findGroupsByUser(String userId) {
        return groupRepository.findGroupsByOwnerOrUsersContaining(new User(userId), new User(userId))
                .stream().map(ModelToGetDTO::tranform).toList();
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


    public GroupGetDTO update(GroupPutDTO groupDTO, Boolean patching) {
        Group oldGroup = groupRepository.findById(groupDTO.getId()).get();
        Archive picture = oldGroup.getPicture();


        Group group = patching ? oldGroup : new Group();
        User owner = oldGroup.getOwner();
        autoMapper.map(groupDTO, group, patching);
        group.setOwner(owner);

        //TODO: verificar se ao adicionar em membro se ele terá uma permission
        Group groupOld = groupRepository.findById(group.getId()).get();
        Collection<Permission> permissions = group.getPermissions().stream().filter(p ->
                !groupOld.getPermissions().contains(p)
        ).toList();
        for(Permission permission : permissions) {
            updatePermission(group, permission);
        }
        group.setPicture(picture);
        Collection<User> usersAddedAndRemoved = new ArrayList<>(group.getUsers().stream().filter(u ->
                !groupOld.getUsers().contains(u)
        ).toList());
        usersAddedAndRemoved.addAll(groupOld.getUsers().stream().filter(u ->
                !group.getUsers().contains(u)
        ).toList());
        usersAddedAndRemoved.forEach(u -> {
            if(!u.equals(group.getOwner())){
                //TODO: mudar para passar o id do user
                notificationService.generateNotification(TypeOfNotification.ADDORREMOVEINGROUP, 0L, group.getId());
            }
        });
        return ModelToGetDTO.tranform(groupRepository.save(group));
    }


    private User updatePermissionInAUser(User userDTO, Permission permission){
        User user = userRepository.findByUserDetailsEntity_Username(userDTO.getUserDetailsEntity().getUsername()).get();
        Collection<Permission> permissions = user.getPermissions();
        if(user.getPermissions() != null) {
            Permission oldPermission = user.getPermissions().stream().filter(p ->
                    p.getProject().getId().equals(permission.getProject().getId())).findFirst().orElse(null);
            if(oldPermission != null ){
                if(!oldPermission.equals(permission)){
                    //TODO:mudar para passar o id do user
                    notificationService.generateNotification(TypeOfNotification.CHANGEPERMISSION, 0L, permission.getProject().getId());
                }
                permissions.remove(oldPermission);
            }
        }else{
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        user.setPermissions(permissions);
        return user;
    }


    private void updatePermission(Group group, Permission permission) {
        Collection <User> users = group.getUsers().stream().map( u -> {
//            User user = updatePermissionInAUser((userRepository.findById(u.getId()).get()), permission);
//            System.out.println(user.getUserDetailsEntity().getPassword());
            User user = updatePermissionInAUser(u, permission);
            userRepository.save(user);
            return user;
        }).toList();
        group.setUsers(users);
//        User owner = updatePermissionInAUser(userRepository.findById(group.getOwner().getId()).get(), permission);

        User owner = updatePermissionInAUser(group.getOwner(), permission);
        userRepository.save(owner);
        group.setOwner(owner);
    }


    public void delete(Long id) {
        groupRepository.deleteById(id);
    }


    public GroupGetDTO updatePicture(MultipartFile picture, Long id) {
        Group group = groupRepository.findById(id).get();
        group.setPicture(new Archive(picture));
        return ModelToGetDTO.tranform(groupRepository.save(group));
    }


}

