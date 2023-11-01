package br.demo.backend.controller;

import br.demo.backend.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public UserModel findOne(@RequestParam Long id){
        return userService.findOne(id);
    }

    @GetMapping
    public List<UserModel> findAll(){
        return userService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        userService.delete(id);
    }


}
