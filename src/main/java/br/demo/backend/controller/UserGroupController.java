package br.demo.backend.controller;


import br.demo.backend.model.UserGroupModel;
import br.demo.backend.service.UserGroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    public UserGroupModel findOne(@PathVariable Long id){
        return userGroupService.findOne(id);
    }

    @GetMapping
    public Collection<UserGroupModel> findAll(){
        return userGroupService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userGroupService.delete(id);
    }

}
