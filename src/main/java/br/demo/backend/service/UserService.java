package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ModelToGetDTO;
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
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private AutoMapper<User> autoMapper;

    public Collection<UserGetDTO> findAll() {
        return userRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public UserGetDTO findOne(String id) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        return ModelToGetDTO.tranform(user);
    }

    public void save(UserPostDTO userDto) {
        try{
            userRepository.findByUserDetailsEntity_Username(userDto.getUserDetailsEntity().getUsername()).get();
            throw new IllegalArgumentException("Username is already been using by other user0");
        }catch (NoSuchElementException e){
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setConfiguration(new Configuration());
            userRepository.save(user);
        }
    }
    public void update(UserPutDTO userDTO, Boolean patching) {
        User oldUser = userRepository.findById(userDTO.getId()).get();
        System.out.println(userDTO);
        User user = patching ? oldUser : new User();
        autoMapper.map(userDTO, user, patching);

        user.setPicture(oldUser.getPicture());
        user.setPoints(oldUser.getPoints());
        user.getUserDetailsEntity().setPassword(oldUser.getUserDetailsEntity().getPassword());
        userRepository.save(user);
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
        userRepository.save(user);
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


    public Collection<UserGetDTO> findByUserNameOrName(String name) {
        return userRepository.findByUserDetailsEntity_UsernameContainingOrNameContaining(name, name).stream().map(ModelToGetDTO::tranform).toList();
    }
}
