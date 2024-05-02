package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.service.GroupService;
import br.demo.backend.utils.IdGroupValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;
    private IdGroupValidation validation;

    //Precisa estar logado
    @PostMapping
    public GroupGetDTO insert(@RequestBody GroupPostDTO group) {
        return groupService.save(group);
    }
    @PutMapping("/{groupId}")
    public GroupGetDTO upDate(@RequestBody GroupPutDTO group, @PathVariable Long groupId) {
        validation.of(groupId, group.getId());
        return groupService.update(group, false);
    }
    @PatchMapping("/{groupId}")
    public GroupGetDTO patch(@RequestBody GroupPutDTO group, @PathVariable Long groupId) {
        validation.of(groupId, group.getId());
        return groupService.update(group, true);
    }

    @GetMapping("/{groupId}")
    public GroupGetDTO findOne(@PathVariable Long groupId) {
        return groupService.findOne(groupId);
    }
    @GetMapping("/my")
    public Collection<SimpleGroupGetDTO> findGroupsByAUser() {
        return groupService.findGroupsByUser();
    }

    @GetMapping("/project/{projectId}")
    public Collection<SimpleGroupGetDTO> findGroupsByAProject(@PathVariable Long projectId) {
        return groupService.findGroupsByAProject(projectId);
    }

    @DeleteMapping("/{groupId}")
    public void delete(@PathVariable Long groupId) {
        groupService.delete(groupId);
    }
    @PatchMapping("/{groupId}/picture")
    public GroupGetDTO updatePicture(@RequestParam MultipartFile picture, @PathVariable Long groupId) {
        return groupService.updatePicture(picture, groupId);
    }

    @PatchMapping("/{groupId}/change-owner")
    public GroupGetDTO updateOwner(@RequestBody OtherUsersDTO newOwner, @PathVariable Long groupId) {
        return groupService.updateOwner(newOwner, groupId);
    }

    @PostMapping("/{groupId}/add-user/{userId}")
    public void addUser(@PathVariable Long userId, @PathVariable Long groupId) {
         groupService.inviteUser( groupId, userId);
    }

    @GetMapping
    public Collection<SimpleGroupGetDTO> getAllGroups(){return groupService.findAll();}
}
