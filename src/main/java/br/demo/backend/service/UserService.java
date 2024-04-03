package br.demo.backend.service;


import br.demo.backend.exception.HeHaveGroupsException;
import br.demo.backend.exception.HeHaveProjectsException;
import br.demo.backend.model.*;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.model.dtos.user.UserPutDTO;
import br.demo.backend.repository.PermissionRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private ProjectRepository projectRepository;
    private AutoMapper<User> autoMapper;
    private NotificationService notificationService;
    public UserGetDTO findOne(String id) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        return ModelToGetDTO.tranform(user);
    }

    public UserGetDTO save(UserPostDTO userDto) {
        try{
            userRepository.findByUserDetailsEntity_Username(userDto.getUserDetailsEntity().getUsername()).get();
            throw new IllegalArgumentException("Username is already been using by other user0");
        }catch (NoSuchElementException e){
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setConfiguration(new Configuration());
            return ModelToGetDTO.tranform(userRepository.save(user));
        }
    }
    public UserGetDTO update(UserPutDTO userDTO, Boolean patching) throws AccessDeniedException {
        User oldUser = userRepository.findById(userDTO.getId()).get();
        String username = ((UserDatailEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if(!oldUser.getUserDetailsEntity().getUsername().equals(username)){
            throw new AccessDeniedException("You can't update another user");
        };
        System.out.println(userDTO);
        User user = patching ? oldUser : new User();
        autoMapper.map(userDTO, user, patching);

        user.setPicture(oldUser.getPicture());
        user.setPoints(oldUser.getPoints());
        user.getUserDetailsEntity().setPassword(oldUser.getUserDetailsEntity().getPassword());
        return ModelToGetDTO.tranform(userRepository.save(user));
    }

    public void delete()  {
        String username = ((UserDatailEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Collection<Project> projects = projectRepository.findProjectsByOwner_UserDetailsEntity_Username(username);
        if(!projects.isEmpty()){
            throw new HeHaveProjectsException();
        }
        Collection<Group> groups = groupRepository.findGroupsByOwner_UserDetailsEntity_Username(username);
        if(!groups.isEmpty()){
            throw new HeHaveGroupsException();
        }
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        userRepository.deleteById(user.getId());
    }

    public PermissionGetDTO getPermissionOfAUserInAProject(String userId, Long projectId){
        User user = userRepository.findByUserDetailsEntity_Username(userId).get();
        Permission permission = user.getPermissions().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
        return ModelToGetDTO.tranform(permission);
    }


    public UserGetDTO updatePicture(MultipartFile picture) {
        String username = ((UserDatailEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        user.setPicture(new Archive(picture));
        return ModelToGetDTO.tranform(userRepository.save(user));

    }

    public UserGetDTO updatePassword(String id, String password) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        user.getUserDetailsEntity().setPassword(password);
        return ModelToGetDTO.tranform(userRepository.save(user));
    }

//    @Transactional
//    public void putNewPermissionInAProject(String userId, Long permissionId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        if (user.getPermissions() != null){
//            user.getPermissions().clear();
//        }
//
//        user.getPermissions().add(permissionRepository.findById(permissionId).get());
//
//        userRepository.save(user);
//    }




    public Collection<UserGetDTO> findAll(){
        return userRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public UserGetDTO addPoints(User user, Long points) {
        List<Long> targets = List.of(1000L, 5000L, 10000L, 15000L, 30000L, 50000L, 100000L, 200000L, 500000L, 1000000L);


        user.setPoints(user.getPoints() + points);
        UserGetDTO userGetDTO = ModelToGetDTO.tranform(userRepository.save(user));
        for (Long target : targets) {
            if (user.getPoints() < target && user.getPoints() + points >= target) {
                notificationService.generateNotification(TypeOfNotification.POINTS, user.getId(), target);
            }
        }
        return userGetDTO;
    }

    public UserGetDTO findLogged() {
        String username = ((UserDatailEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return ModelToGetDTO.tranform(userRepository.findByUserDetailsEntity_Username(username).get());
    }

    public PermissionGetDTO updatePermission(String username, Permission permission) {
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        Collection<Permission> permissions = user.getPermissions();
        if(user.getPermissions() != null) {
            Permission oldPermission = user.getPermissions().stream().filter(p ->
                    p.getProject().getId().equals(permission.getProject().getId())).findFirst().orElse(null);
            if(oldPermission != null ) {
                if (!oldPermission.equals(permission)) {
                    notificationService.generateNotification(TypeOfNotification.CHANGEPERMISSION,user.getId(), permission.getProject().getId());
                }
                permissions.remove(oldPermission);
            }
        } else {
            permissions = List.of();
        }
        permissions.add(permission);
        user.setPermissions(permissions);
        return ModelToGetDTO.tranform(permission);
    }
}

