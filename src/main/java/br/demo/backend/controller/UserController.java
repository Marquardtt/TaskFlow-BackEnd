package br.demo.backend.controller;

import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.model.dtos.user.UserPutDTO;
import br.demo.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    // TODO: 16/02/2024 O Usuario esta perdendo algumas de suas props

    @PostMapping
    public void insert(@RequestBody UserPostDTO user){
        userService.save(user);
    }

    @GetMapping("/{username}/project/{projectId}")
    public PermissionGetDTO getPermisisonInAProject(@PathVariable String username, @PathVariable Long projectId){
        return userService.getPermissionOfAUserInAProject(username, projectId);
    }

    @PutMapping
    public void upDate(@RequestBody UserPutDTO user){
        userService.update(user, false);
    }
    @PatchMapping
    public void patch(@RequestBody UserPutDTO user){
        userService.update(user, true);
    }

    @GetMapping("/{username}")
    public UserGetDTO findOne(@PathVariable String username){
        return userService.findOne(username);
    }


    @PatchMapping("/picture/{username}")
    public void upDatePicture(@RequestParam MultipartFile picture, @PathVariable String username) {
        userService.updatePicture(picture, username);
    }

    @PatchMapping("/password/{username}")
    public void upDatePassword(@PathVariable String username, @RequestBody String password) {
        userService.updatePassword(username, password);
    }

    @GetMapping
    public Collection<UserGetDTO> findAll(){
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    @GetMapping("/name/{name}")
    public Collection<UserGetDTO> findByUsersNameOrName(@PathVariable String name) {
        return userService.findByUserNameOrName(name);
    }

}
