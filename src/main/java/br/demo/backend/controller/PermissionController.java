package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.permission.PermissionPostDTO;
import br.demo.backend.model.dtos.permission.PermissionPutDTO;
import br.demo.backend.service.PermissionService;
import br.demo.backend.utils.IdProjectValidation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private PermissionService permissionService;
    private IdProjectValidation validation;
    @PostMapping("/project/{projectId}")
    public PermissionGetDTO insert(@RequestBody PermissionPostDTO permissionDTO, @PathVariable Long projectId){
        validation.of(projectId, permissionDTO.getProject().getId());
        return permissionService.save(permissionDTO, projectId);
    }

    @PutMapping("/project/{projectId}")
    public PermissionGetDTO upDate(@RequestBody PermissionPutDTO permission, @PathVariable Long projectId){
        return permissionService.update(permission, false, projectId);
    }
    @PatchMapping("/project/{projectId}")
    public PermissionGetDTO patch(@RequestBody PermissionPutDTO permission, @PathVariable Long projectId){
        return permissionService.update(permission, true, projectId);
    }

    @GetMapping("/project/{projectId}")
    public Collection<PermissionGetDTO> findAll(@PathVariable Long projectId){
        return permissionService.findByProject(projectId);
    }

    @DeleteMapping("/{id}/other/{substituteId}/project/{projectId}")
    public void delete(@PathVariable Long id, @PathVariable Long substituteId, @PathVariable Long projectId){
        permissionService.delete(id, substituteId, projectId);
    }

}
