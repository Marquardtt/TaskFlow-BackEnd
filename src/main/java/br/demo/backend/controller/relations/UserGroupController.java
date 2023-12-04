package br.demo.backend.controller.relations;


import br.demo.backend.model.relations.PermissionProject;
import br.demo.backend.service.relations.UserProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user-group")
public class UserGroupController {
    private UserProjectService userGroupService;

    @PostMapping
    public void insert(@RequestBody PermissionProject user){
        userGroupService.save(user);
    }

    @PutMapping
    public void upDate(@RequestBody PermissionProject user){
        userGroupService.save(user);
    }

    @GetMapping("/{id}")
    public PermissionProject findOne(@PathVariable Long id){
        return userGroupService.findOne(id);
    }

    @GetMapping
    public Collection<PermissionProject> findAll(){
        return userGroupService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userGroupService.delete(id);
    }

}
