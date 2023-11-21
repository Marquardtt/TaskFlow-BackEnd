package br.demo.backend.controller;


import br.demo.backend.model.UserGroupId;
import br.demo.backend.model.UserGroup;
import br.demo.backend.service.UserGroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user-group")
public class UserGroupController {
    private UserGroupService userGroupService;

    @PostMapping
    public void insert(@RequestBody UserGroup user){
        userGroupService.save(user);
    }

    @PutMapping
    public void upDate(@RequestBody UserGroup user){
        userGroupService.save(user);
    }

    @GetMapping("/{id}/{id2}")
    public UserGroup findOne(@PathVariable Long id, @PathVariable Long id2){
        return userGroupService.findOne(new UserGroupId(id, id2));
    }

    @GetMapping
    public Collection<UserGroup> findAll(){
        return userGroupService.findAll();
    }

    @DeleteMapping("/{id}/{id2}")
    public void delete(@PathVariable Long id,@PathVariable Long id2){
        userGroupService.delete(new UserGroupId(id, id2));
    }

}
