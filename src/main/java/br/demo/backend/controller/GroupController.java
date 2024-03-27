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

    //Precisa estar logado
    @PostMapping
    public GroupGetDTO insert(@RequestBody GroupPostDTO group) {
        return groupService.save(group);
    }

    //Precisa ser o owner do grupo
    @PutMapping
    public GroupGetDTO upDate(@RequestBody GroupPutDTO group) {

        return groupService.update(group, false);
    }
    //Precisa ser o owner do grupo
    @PatchMapping
    public GroupGetDTO patch(@RequestBody GroupPutDTO group) {

        return groupService.update(group, true);
    }

    //Precisa que o usuario logado seja o mesmo do parametro
    @GetMapping("/user/{userId}")
    public Collection<GroupGetDTO> findGroupsByAUser(@PathVariable String userId) {
        return groupService.findGroupsByUser(userId);
    }

    //Precisa ser o owner do group ou ser um membro
    @GetMapping("/{groupId}/permissions/{projectId}")
    public ResponseEntity<Collection<Permission>> findAllPermissionsOfAGroupInAProject(@PathVariable Long groupId, @PathVariable Long projectId) {
        Collection<Permission> permissions = groupService.findAllPermissionsOfAGroupInAProject(groupId, projectId);
        return ResponseEntity.ok(permissions);
    }

    //Precisa ser o owner do group
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

    //Precisa ser o owner do group
    @PatchMapping("/picture/{id}")
    public GroupGetDTO updatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        return groupService.updatePicture(picture, id);
    }

    //Precisa ser o owner do group
    @PatchMapping("/change-owner/{projectId}")
    public GroupGetDTO updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        return groupService.updateOwner(newOwner, projectId);
    }
}
