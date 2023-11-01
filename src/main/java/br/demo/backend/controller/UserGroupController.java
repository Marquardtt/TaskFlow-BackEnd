package br.demo.backend.controller;


import br.demo.backend.model.UserGroupModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user-group")
public class UserGroupController {
    private UserGroupService userGroupService;

    @PostMapping
    public void insert(@RequestBody UserGroupModel user){
        userGroupService.save(user);
    }

    @PutMapping
    public void upDate(@RequestBody UserGroupModel user){
        userGroupService.save(user);
    }

    @GetMapping("/{id}")
    public UserGroupModel findOne(@RequestParam Long id){
        return userGroupService.findOne(id);
    }

    @GetMapping
    public List<UserGroupModel> findAll(){
        return userGroupService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        userGroupService.delete(id);
    }

}
