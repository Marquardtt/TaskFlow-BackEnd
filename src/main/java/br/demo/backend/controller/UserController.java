package br.demo.backend.controller;

import br.demo.backend.model.UserModel;
import br.demo.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @PostMapping
    public void insert(@RequestBody UserModel user){
        userService.save(user);
    }

    @PutMapping
    public void upDate(@RequestBody UserModel user){
        userService.save(user);
    }

    @GetMapping("/{id}")
    public UserModel findOne(@PathVariable Long id){
        return userService.findOne(id);
    }

    @GetMapping
    public Collection<UserModel> findAll(){
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }


}
