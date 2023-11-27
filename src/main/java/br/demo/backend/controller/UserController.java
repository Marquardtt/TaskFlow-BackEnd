package br.demo.backend.controller;

import br.demo.backend.model.User;
import br.demo.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public void upDate(@RequestBody User user){
        userService.save(user);
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

    @GetMapping
    public Collection<User> findAll(){
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }


}
