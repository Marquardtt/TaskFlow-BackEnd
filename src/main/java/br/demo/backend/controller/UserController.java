package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.model.dtos.user.UserPutDTO;
import br.demo.backend.service.UserService;
import br.demo.backend.utils.IdProjectValidation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private IdProjectValidation validation;

    @PostMapping
    public UserGetDTO insert(@RequestBody UserPostDTO user){
        return userService.save(user);
    }

    @PutMapping
    public UserGetDTO upDate(@RequestBody UserPutDTO user) throws AccessDeniedException {
        return userService.update(user, false);
    }
    @PatchMapping
    public UserGetDTO patch(@RequestBody UserPutDTO user) throws AccessDeniedException {
        return userService.update(user, true);
    }

    @GetMapping("/{username}")
    public OtherUsersDTO findOne(@PathVariable String username){
        return userService.findOne(username);
    }

    //precisa estar logado
    @GetMapping("/logged")
    public UserGetDTO findLogged(){
        return userService.findLogged();
    }

    @PatchMapping("/picture")
    public UserGetDTO upDatePicture(@RequestParam MultipartFile picture) {
        return userService.updatePicture(picture);
    }

    //FEITO
    @PatchMapping("/password/{username}")
    public UserGetDTO upDatePassword(@PathVariable String username, @RequestParam String password) {
        System.out.println(password);
        return userService.updatePassword(username, password);
    }
    @DeleteMapping
    public void delete() throws AccessDeniedException {
        userService.delete();
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    @GetMapping
    public Collection<OtherUsersDTO> findAll() {
        return userService.findAll();
    }

  //TODO: fazer isso no security
    //precisa ser o dono do grupo em que o usuario esta nesse projeto
    @PatchMapping("{username}/update-permission/project/{projectId}")
    public PermissionGetDTO updatePermission(@PathVariable String username, @PathVariable Long projectId, @RequestBody Permission permission){
        validation.ofObject(projectId, permission.getProject());
        return userService.updatePermissionOfAUser(username, permission);
    }
}
