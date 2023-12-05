package br.demo.backend.controller;

import br.demo.backend.model.Group;
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

    @PutMapping("/permission/{projectId}/{permissionId}")
    public void updatePermission(@RequestBody Group group, @PathVariable Long projectId, @PathVariable Long permissionId) {
        groupService.updatePermission(group, projectId, permissionId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

}
