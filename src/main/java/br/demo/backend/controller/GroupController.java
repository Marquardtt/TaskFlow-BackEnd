package br.demo.backend.controller;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.Permission;
import br.demo.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        groupService.update(group, false);
    }
    @PatchMapping
    public void patch(@RequestBody Group group) {
        groupService.update(group, true);
    }

    @PutMapping("/user/{groupId}")
    public void updateUser(@RequestBody User user, @PathVariable Long groupId) {
        groupService.updateUsers(user, groupId);
    }

    @PutMapping("/{groupId}/{permission}")
    public void updatePermission( @RequestBody Group group, @RequestBody Permission permission) {
        groupService.updatePermission(group, permission);
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

    @GetMapping("/{groupId}/{projectId}")
    public Permission findPermissionOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        return groupService.findPermissionOfAGroupInAProject(groupId, projectId);
    }

    @GetMapping("/{groupId}/permissions/{projectId}")
    public ResponseEntity<Collection<Permission>> findAllPermissionsOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        Collection<Permission> permissions = groupService.findAllPermissionsOfAGroupInAProject(groupId, projectId);
        return ResponseEntity.ok(permissions);
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
