package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
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

    @PostMapping
    public void insert(@RequestBody UserPostDTO user){
        userService.save(user);
    }


    @GetMapping("/{username}/{projectId}")
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

    @GetMapping("/username/{username}/{password}")
    public UserGetDTO findByUsernameAndPassword(@PathVariable String username, @PathVariable String password){
        return userService.findByUsernameAndPassword(username, password);
    }
    @GetMapping("/email/{email}/{password}")
    public UserGetDTO findByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return userService.findByEmailAndPassword(email, password);
    }
    @PatchMapping("/picture/{username}")
    public void upDatePicture(@RequestParam MultipartFile picture, @PathVariable String username) {
        userService.updatePicture(picture, username);
    }

    @GetMapping
    public Collection<UserGetDTO> findAll(){
        return userService.findAll();
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username){
        userService.delete(username);
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    @GetMapping("/name/{name}")
    public Collection<UserGetDTO> findByUsersNameOrName(@PathVariable String name) {
        return userService.findByUserNameOrName(name);
    }

    //TODO:Requisição que atualiza a senha dele
}
