package br.demo.backend.controller;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;
    @PostMapping
    public void insert(@RequestBody Group group) {
        groupService.save(group);
    }

    @PutMapping
    public void upDate(@RequestBody Group group) {
        groupService.update(group);
    }

    @PutMapping("/users/{groupId}")
    public void updateUser(@RequestBody User user, @PathVariable Long groupId) {
        groupService.updateUsers(user, groupId);
    }

    @GetMapping("/{id}")
    public Group findOne(@PathVariable Long id) {
        return groupService.findOne(id);
    }

    @GetMapping
    public Collection<Group> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/user/{userId}")
    public Collection<Group> findGroupsByAUser(@PathVariable Long userId) {
        return groupService.findGroupsByUser(userId);
    }
    @GetMapping("/project/{projectId}")
    public Collection<Group> findGroupsOfAProject(@PathVariable Long projectId) {
        return groupService.findGroupsOfAProject(projectId);
    }

    @GetMapping("/{goupId}/{projectId}")
    public Permission insert(@PathVariable Long goupId, @PathVariable Long projectId){
        return groupService.getPermissionOfAGroupInAProject(goupId, projectId);
    }

    @GetMapping("/users/{groupId}")
    public Collection<User> findUsersByGroup(@PathVariable Long groupId) {
        return groupService.findUsersByGroup(groupId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }




}
