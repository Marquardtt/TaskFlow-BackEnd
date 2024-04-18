package br.demo.backend.service;


import br.demo.backend.exception.UserCantBeAddedInThisGroupException;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;

import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;

import br.demo.backend.model.interfaces.WithMembers;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class GroupService {


    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;
    ;
    private AutoMapper<Group> autoMapper;
    private UserService userService;


    public GroupGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(groupRepository.findById(id).get());
    }


    public GroupGetDTO save(GroupPostDTO groupDto) {
           String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
           User user = userRepository.findByUserDetailsEntity_Username(username).get();
           Group group = new Group();
           BeanUtils.copyProperties(groupDto, group);

               group.setOwner(user);
               if (group.getPermissions() != null ) {
                   if(group.getUsers() != null){
                       updatePermission(group, group.getPermissions().stream().findFirst().get());
                   }
                   group.setPermissions(groupDto.getPermissions());

               }
               return ModelToGetDTO.tranform(groupRepository.save(group));
    }

    private void setTheMembers(Group group, WithMembers groupDTO) {
        group.setUsers(groupDTO.getMembersDTO().stream().map(u -> {
            User user = userRepository.findByUserDetailsEntity_Username(u.getUsername()).get();
            return user;
        }).toList());

    }


    public GroupGetDTO updateOwner(OtherUsersDTO userDto, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(userDto.getId()).get();
        group.setOwner(user);
        return ModelToGetDTO.tranform(groupRepository.save(group));
    }

    public Collection<SimpleGroupGetDTO> findGroupsByUser() {
        UserDatailEntity userDatail = (UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUserDetailsEntity_Username(userDatail.getUsername()).get();
        return groupRepository.findGroupsByOwnerOrUsersContaining(user, user)
                .stream().map(ModelToGetDTO::tranformSimple).toList();
    }

    public Collection<SimpleGroupGetDTO> findGroupsByAProject(Long projectId) {
        return groupRepository.findGroupsByPermissions_Project(new Project(projectId))
                .stream().map(ModelToGetDTO::tranformSimple).toList();
    }


    public GroupGetDTO update(GroupPutDTO groupDTO, Boolean patching) {
        Group oldGroup = groupRepository.findById(groupDTO.getId()).get();
        //this is to keep the old group, to keep the owner and the picture and use patch our put
        Group group = new Group();
        if(patching) BeanUtils.copyProperties(oldGroup, group);
        autoMapper.map(groupDTO, group, patching);

        if (group.getUsers() == null) {
            System.out.println(1);
            System.out.println("tô aqui");
            if (groupDTO.getUsers() != null){

                Collection<User> users = new ArrayList<>();
                for (UserGetDTO userGetDTO : groupDTO.getUsers()) {
                    System.out.println(2);
                    System.out.println("agora aqui");
                    User user = new User();
                    BeanUtils.copyProperties(userGetDTO, user);
                    users.add(user);
                }
                group.setUsers(users);
            } else{
                System.out.println(3);
                System.out.println("vazio, doideira");
            }

        }

        if (group.getUsers() != null){
           System.out.println(4);
           System.out.println("entrei");
            setTheMembers(group, groupDTO);
        }


        keepFields(group, oldGroup);

        //this get the new permissions of the group and update the permission to the users
        if (group.getPermissions() != null){
            group.getPermissions().stream().filter(p ->
                    !oldGroup.getPermissions().contains(p)
            ).forEach(p -> updatePermission(group, p));
        }

        //saving and generating notifications
        GroupGetDTO groupGetDTO = ModelToGetDTO.tranform(groupRepository.save(group));
        notificationsAddOrRemove(group, oldGroup);
        return groupGetDTO;
    }

    private void notificationsAddOrRemove(Group group, Group groupOld) {
        Collection <User> removed = groupOld.getUsers().stream().filter(u ->
                !group.getUsers().contains(u)
        ).toList();
        //this generates the notification to add ou remove someone
        removed.forEach(u -> {
            if (!u.equals(group.getOwner())) {
                notificationService.generateNotification(TypeOfNotification.REMOVEINGROUP, u.getId(), group.getId());
            }
        });
    }

    private void keepFields(Group group, Group oldGroup) {
        group.setOwner(oldGroup.getOwner());
        group.setPicture(oldGroup.getPicture());
        if(group.getUsers() == null){
            System.out.println(5);
            System.out.println("vaziooooooooooooooo");
            group.setUsers(new ArrayList<>());
        }
        if (oldGroup.getUsers() == null){
            System.out.println("doideiraaaaaaaaaaaaaaaaaaaa");
            oldGroup.setUsers(new ArrayList<>());
        }
    }

    private void updatePermission(Group group, Permission permission) {
        //here we update the permission on members of the group
        group.getUsers().forEach(u -> {
            userService.updatePermissionOfAUser(u.getUserDetailsEntity().getUsername(), permission);
        });
        if (group.getOwner() != null) {
            //here we update the permission of the group owner

            userService.updatePermissionOfAUser(group.getOwner().getUserDetailsEntity().getUsername(), permission);
        }
    }


    public void delete(Long id) {
        groupRepository.deleteById(id);
    }


    public GroupGetDTO updatePicture(MultipartFile picture, Long id) {
        Group group = groupRepository.findById(id).get();
        group.setPicture(new Archive(picture));
        return ModelToGetDTO.tranform(groupRepository.save(group));
    }

    public void inviteUser(Long groupId, Long userId) {
        User user = userRepository.findById(userId).get();
        Group group = groupRepository.findById(groupId).get();
        if(group.getPermissions().stream().anyMatch(p -> user.getPermissions().stream().anyMatch(p2 -> p.getProject().equals(p2.getProject())))){
            throw new UserCantBeAddedInThisGroupException();
        }
        notificationService.generateNotification(TypeOfNotification.ADDINGROUP, userId, groupId);
    }
}

