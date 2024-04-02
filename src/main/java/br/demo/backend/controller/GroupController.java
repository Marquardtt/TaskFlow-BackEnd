package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.GroupPostDTO;
import br.demo.backend.model.dtos.group.GroupPutDTO;
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

    //Precisa estar logado
    @PostMapping
    public GroupGetDTO insert(@RequestBody GroupPostDTO group) {
        return groupService.save(group);
    }

    @PutMapping("/{groupId}")
    public void upDate(@RequestBody GroupPutDTO group) {
        groupService.update(group, false);
    }
    @PatchMapping("/{groupId}")
    public void patch(@RequestBody GroupPutDTO group) {
        groupService.update(group, true);
    }

    @GetMapping("/{groupId}")
    public GroupGetDTO findOne(@PathVariable Long id) {
        return groupService.findOne(id);
    }
    @GetMapping
    public Collection<GroupGetDTO> findGroupsByAUser() {
        return groupService.findGroupsByUser();
    }

    //Precisa ser o owner do group ou ser um membro
    //TODO: Verficar se precisa mesmo dessa requisição
    @GetMapping("/{groupId}/permissions/{projectId}")
    public ResponseEntity<Collection<Permission>> findAllPermissionsOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        Collection<Permission> permissions = groupService.findAllPermissionsOfAGroupInAProject(groupId, projectId);
        return ResponseEntity.ok(permissions);
    }

    @DeleteMapping("/{groupId}")
    public void delete(@PathVariable Long groupId) {
        groupService.delete(groupId);
    }
    @PatchMapping("/{groupId}/picture")
    public void updatePicture(@RequestParam MultipartFile picture, @PathVariable Long groupId) {
        groupService.updatePicture(picture, groupId);
    }

    @PatchMapping("/{groupId}/change-owner")
    public void updateOwner(@RequestBody User newOwner, @PathVariable Long groupId) {
        groupService.updateOwner(newOwner, groupId);

    }
}
