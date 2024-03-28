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

    //   FEITO
    @PostMapping
    public UserGetDTO insert(@RequestBody UserPostDTO user){
        return userService.save(user);
    }

    //  FEITO => PORÉM PROVÁVEL QUE TERÁ ALTERAÇÕES
    @GetMapping("/{username}/{projectId}")
    public PermissionGetDTO getPermisisonInAProject(@PathVariable String username, @PathVariable Long projectId){
        return userService.getPermissionOfAUserInAProject(username, projectId);
    }

    //   FEITO
    @PutMapping
    public UserGetDTO upDate(@RequestBody UserPutDTO user){
        return userService.update(user, false);
    }
    //   FEITO
    @PatchMapping
    public UserGetDTO patch(@RequestBody UserPutDTO user) {
        return userService.update(user, true);
    }

    //    precisa estar num mesmo projeto ou group que o outro user   IMPLEMENTAR O ACCESS => BECKER
    @GetMapping("/{username}")
    public UserGetDTO findOne(@PathVariable String username){
        return userService.findOne(username);
    }

    //FEITO
    @PatchMapping("/picture/{username}")
    public UserGetDTO upDatePicture(@RequestParam MultipartFile picture, @PathVariable String username) {
        return userService.updatePicture(picture, username);
    }

    //FEITO
    @PatchMapping("/password/{username}")
    public UserGetDTO upDatePassword(@PathVariable String username, @RequestBody String password) {
        return userService.updatePassword(username, password);
    }

    //FEITO
    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username){
        userService.delete(username);
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    //FEITO
    @GetMapping("/name/{name}")
    public Collection<UserGetDTO> findByUsersNameOrName(@PathVariable String name) {
        return userService.findByUserNameOrName(name);
    }

}
