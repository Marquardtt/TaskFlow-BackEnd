package br.demo.backend.service;




import br.demo.backend.exception.GroupNotFoundException;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.security.entity.UserDatailEntity;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserService userService;


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
    public Collection<GroupGetDTO> findGroupsByUser() {
        UserDatailEntity userDatail = (UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUserDetailsEntity_Username(userDatail.getUsername()).get();
        return groupRepository.findGroupsByOwnerOrUsersContaining(user, user)
                .stream().map(ModelToGetDTO::tranform).toList();
    }

//
//    public PermissionGetDTO findPermissionOfAGroupInAProject(Long groupId, Long projectId) {
//        Group group = groupRepository.findById(groupId).get();
//        Permission permission = group.getPermissions().stream().filter(
//                p -> p.getProject().getId().equals(projectId)
//        ).findFirst().get();
//        return ModelToGetDTO.tranform(permission);
//    }
//
//


    public Collection<Permission> findAllPermissionsOfAGroupInAProject(Long groupId, Long projectId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        return group.getPermissions().stream()
                .filter(permission -> permission.getProject().getId().equals(projectId)).toList();
    }


    public GroupGetDTO update(GroupPutDTO groupDTO, Boolean patching) {
        Group oldGroup = groupRepository.findById(groupDTO.getId()).get();
        Archive picture = oldGroup.getPicture();

        Group group = patching ? oldGroup : new Group();
        autoMapper.map(groupDTO, group, patching);

        //keep the old owner
        group.setOwner(oldGroup.getOwner());

        //this get the new permissions of the group and update the permission to the users
        // and group, normally that will be just one
        Group groupOld = groupRepository.findById(group.getId()).get();
        group.getPermissions().stream().filter(p ->
                !groupOld.getPermissions().contains(p)
        ).forEach(p ->  updatePermission(group, p));

//        this keep the picture
        group.setPicture(oldGroup.getPicture());
//        this gets the difference users in above lists, getting the added and removed users
        Collection<User> usersAddedAndRemoved = new ArrayList<>(group.getUsers().stream().filter(u ->
                !groupOld.getUsers().contains(u)
        ).toList());
        usersAddedAndRemoved.addAll(groupOld.getUsers().stream().filter(u ->
                !group.getUsers().contains(u)
        ).toList());

        GroupGetDTO groupGetDTO =  ModelToGetDTO.tranform(groupRepository.save(group));
//       this generates the notification to add ou remove someone
        usersAddedAndRemoved.forEach(u -> {
            if(!u.equals(group.getOwner())){
                notificationService.generateNotification(TypeOfNotification.ADDORREMOVEINGROUP, u.getId(), group.getId());
            }
        });
        return groupGetDTO;
    }

    private void updatePermission(Group group, Permission permission) {
//        here we update the permission on members of the group
        group.getUsers().forEach( u -> {
            userService.updatePermission(u.getUserDetailsEntity().getUsername(), permission);
        });
        //here we update the permission of the group owner
        //TODO: qual é a permissão do owner do grupo (acho que deveria ser todas)
        userService.updatePermission(group.getOwner().getUserDetailsEntity().getUsername(), permission);
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

