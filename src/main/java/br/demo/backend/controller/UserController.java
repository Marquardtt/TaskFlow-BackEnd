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

    //    precisa de nada
    @PostMapping
    public void insert(@RequestBody UserPostDTO user){
        userService.save(user);
    }

    //    precisa estar logado e pertencer a esse projeto
    @GetMapping("/{username}/{projectId}")
    public PermissionGetDTO getPermisisonInAProject(@PathVariable String username, @PathVariable Long projectId){
        return userService.getPermissionOfAUserInAProject(username, projectId);
    }

    //    precisa estar logado e ser o usuario
    @PutMapping
    public void upDate(@RequestBody UserPutDTO user){
        userService.update(user, false);
    }
    //    precisa estar logado e ser o usuario
    @PatchMapping
    public void patch(@RequestBody UserPutDTO user){
        userService.update(user, true);
    }

    //    precisa estar num mesmo projeto ou group que o outro user
    @GetMapping("/{username}")
    public UserGetDTO findOne(@PathVariable String username){
        return userService.findOne(username);
    }

    //precisa ser o usuario logado
    @PatchMapping("/picture/{username}")
    public void upDatePicture(@RequestParam MultipartFile picture, @PathVariable String username) {
        userService.updatePicture(picture, username);
    }

    //precisa ser o usuario logado
    @PatchMapping("/password/{username}")
    public void upDatePassword(@PathVariable String username, @RequestBody String password) {
        userService.updatePassword(username, password);
    }

    //precisa ser o usuario logado
    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username){
        userService.delete(username);
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    //precisa ser o usuario logado
    @GetMapping("/name/{name}")
    public Collection<UserGetDTO> findByUsersNameOrName(@PathVariable String name) {
        return userService.findByUserNameOrName(name);
    }

}
