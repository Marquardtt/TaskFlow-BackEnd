package br.demo.backend.controller;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.Permission;
import br.demo.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //isso deveria ser feito no front
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

    @GetMapping("/{groupId}/{projectId}")
    public Permission findPermissionOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        return groupService.findPermissionOfAGroupInAProject(groupId, projectId);
    }

    @GetMapping("/{groupId}/permissions/{projectId}")
    //um grupo em um projeto so tem uma permissão entao é a mesma coisa que o metodo de cima
    public ResponseEntity<Collection<Permission>> findAllPermissionsOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        Collection<Permission> permissions = groupService.findAllPermissionsOfAGroupInAProject(groupId, projectId);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/users/{groupId}")
    //isso não precisa, group ja tem uma lista de users
    public Collection<User> findUsersByGroup(@PathVariable Long groupId) {
        return groupService.findUsersByGroup(groupId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

    @PatchMapping("/picture/{id}")
    public void updatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        groupService.updatePicture(picture, id);
    }

    @PatchMapping("/change-owner/{projectId}")
    public void updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        groupService.updateOwner(newOwner, projectId);
    }
}
