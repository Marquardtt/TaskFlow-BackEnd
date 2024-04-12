package br.demo.backend.controller;

import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.permission.PermissionPostDTO;
import br.demo.backend.model.dtos.permission.PermissionPutDTO;
import br.demo.backend.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private PermissionService permissionService;
    @PostMapping("/project/{projectId}")
    public PermissionGetDTO insert(@RequestBody PermissionPostDTO permissionDTO, @PathVariable Long projectId){

        return permissionService.save(permissionDTO, projectId);
    }

    @PutMapping("/project/{projectId}")
    public PermissionGetDTO upDate(@RequestBody PermissionPutDTO permission){
        return permissionService.update(permission, false);
    }
    @PatchMapping("/project/{projectId}")
    public PermissionGetDTO patch(@RequestBody PermissionPutDTO permission){
        return permissionService.update(permission, true);
    }

    @GetMapping("/project/{projectId}")
    public Collection<PermissionGetDTO> findAll(@PathVariable Long projectId){
        return permissionService.findByProject(projectId);
    }

    @DeleteMapping("/{id}/other/{substituteId}/project/{projectId}")
    public void delete(@PathVariable Long id, @PathVariable Long substituteId){
        permissionService.delete(id, substituteId);
    }

}
