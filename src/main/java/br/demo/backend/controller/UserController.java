package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
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
    public void insert(@RequestBody User user){
        userService.save(user);
    }


    @GetMapping("/{userId}/{projectId}")
    public Permission insert(@PathVariable Long userId, @PathVariable Long projectId){
        return userService.getPermissionOfAUserInAProject(userId, projectId);
    }

    @PutMapping
    public void upDate(@RequestBody User user){
        userService.update(user, false);
    }
    @PatchMapping
    public void patch(@RequestBody User user){
        userService.update(user, true);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id){
        return userService.findOne(id);
    }

    @GetMapping("/username/{username}/{password}")
    public User findByUsernameAndPassword(@PathVariable String username, @PathVariable String password){
        return userService.findByUsernameAndPassword(username, password);
    }
    @GetMapping("/email/{email}/{password}")

    public User findByEmailAndPassword(@PathVariable String email, @PathVariable String password){
        return userService.findByEmailAndPassword(email, password);
    }
    @PatchMapping("/picture/{id}")
    public void upDatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        userService.updatePicture(picture, id);
    }

    @GetMapping
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    @GetMapping("/name/{name}")
    public Collection<User> findByUsersNameOrName(@PathVariable String name) {
        return userService.findByUserNameOrName(name);
    }
}
