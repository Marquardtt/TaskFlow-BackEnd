package br.demo.backend.service;


import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Configuration;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.model.dtos.user.UserPutDTO;
import br.demo.backend.repository.PermissionRepository;
import br.demo.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
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
    public void update(UserPutDTO userDTO, Boolean patching) {
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User oldUser = userRepository.findById(userDTO.getId()).get();

        System.out.println(userDTO);
        User user = patching ? oldUser : new User();
        autoMapper.map(userDTO, user, patching);

        user.setPicture(oldUser.getPicture());
        user.setPoints(oldUser.getPoints());
        user.getUserDetailsEntity().setPassword(oldUser.getUserDetailsEntity().getPassword());
        return ModelToGetDTO.tranform(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public PermissionGetDTO getPermissionOfAUserInAProject(String userId, Long projectId){
        User user = userRepository.findByUserDetailsEntity_Username(userId).get();
        Permission permission = user.getPermissions().stream().filter(
                p -> p.getProject().getId().equals(projectId)
        ).findFirst().get();
        return ModelToGetDTO.tranform(permission);
    }


    public void updatePicture(MultipartFile picture, String id) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        user.setPicture(new Archive(picture));
        return ModelToGetDTO.tranform(userRepository.save(user));

    }

    public void updatePassword(String id, String password) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        user.getUserDetailsEntity().setPassword(password);
        userRepository.save(user);
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


  
    public UserGetDTO updatePassword(String id, String password) {
        User user = userRepository.findById(id).get();
        user.setPassword(password);
        return ModelToGetDTO.tranform(userRepository.save(user));

    }
    public Collection<UserGetDTO> findAll(){
        return userRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public UserGetDTO addPoints(String username, Integer points) {
        List<Long> targets = List.of(1000L, 5000L, 10000L, 15000L, 30000L, 50000L, 100000L, 200000L, 500000L, 1000000L);

        User user = userRepository.findById(username).get();

        for (Long target : targets) {
            if (user.getPoints() < target && user.getPoints() + points >= target) {
                //TODO: Mudar para 0L ser o id do usuario
                notificationService.generateNotification(TypeOfNotification.POINTS, 0L, target);
            }
        }

        user.setPoints(user.getPoints() + points);
        return ModelToGetDTO.tranform(userRepository.save(user));
    }

    public UserGetDTO findLogged() {
        //TODO: Mudar para pegar o usuario logado
        return ModelToGetDTO.tranform(userRepository.findById("admin").get());
    }

    public PermissionGetDTO updatePermission(String username, Permission permission) {
        User user = userRepository.findById(username).get();
        Collection<Permission> permissions = user.getPermissions();
        if(user.getPermissions() != null) {
            Permission oldPermission = user.getPermissions().stream().filter(p ->
                    p.getProject().getId().equals(permission.getProject().getId())).findFirst().orElse(null);
            if(oldPermission != null ) {
                if (!oldPermission.equals(permission)) {
                   //TODO: mudar para passar o id do user
                    notificationService.generateNotification(TypeOfNotification.CHANGEPERMISSION,0L, permission.getProject().getId());
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

