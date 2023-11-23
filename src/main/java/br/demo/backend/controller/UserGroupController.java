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

    @GetMapping("/{taskId}/{propertyId}")
    public UserGroup findOne(@PathVariable Long taskId, @PathVariable Long propertyId){
        return userGroupService.findOne(new UserGroupId(taskId, propertyId));
    }

    @GetMapping
    public Collection<UserGroup> findAll(){
        return userGroupService.findAll();
    }

    @DeleteMapping("/{taskId}/{propertyId}")
    public void delete(@PathVariable Long taskId,@PathVariable Long propertyId){
        userGroupService.delete(new UserGroupId(taskId, propertyId));
    }

}
