package br.demo.backend.controller;

import br.demo.backend.model.*;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
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
    public void insert(@RequestBody GroupPostDTO group) {
        groupService.save(group);
    }

    @PutMapping
    public void upDate(@RequestBody GroupPutDTO group) {
        groupService.update(group, false);
    }
    @PatchMapping
    public void patch(@RequestBody GroupPutDTO group) {
        groupService.update(group, true);
    }

    @GetMapping("/{id}")
    public GroupGetDTO findOne(@PathVariable Long id) {
        return groupService.findOne(id);
    }

    @GetMapping
    public Collection<GroupGetDTO> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/user/{userId}")
    public Collection<GroupGetDTO> findGroupsByAUser(@PathVariable String userId) {
        return groupService.findGroupsByUser(userId);
    }

    @GetMapping("/{groupId}/permissions/{projectId}")
    public ResponseEntity<Collection<Permission>> findAllPermissionsOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        Collection<Permission> permissions = groupService.findAllPermissionsOfAGroupInAProject(groupId, projectId);
        return ResponseEntity.ok(permissions);
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
