package br.demo.backend.controller;

import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.permission.PermissionPostDTO;
import br.demo.backend.model.dtos.permission.PermissionPutDTO;
import br.demo.backend.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private PermissionService permissionService;
    @PostMapping("/project/{projectId}")
    public void insert(@RequestBody PermissionPostDTO permission){
        permissionService.save(permission);
    }

    @PutMapping("/project/{projectId}")
    public void upDate(@RequestBody PermissionPutDTO permission){
        permissionService.update(permission, false);
    }
    @PatchMapping("/project/{projectId}")
    public void patch(@RequestBody PermissionPutDTO permission){
        permissionService.update(permission, true);
    }

    @GetMapping("/project/{projectId}")
    public Collection<PermissionGetDTO> findAll(@PathVariable Long projectId){
        return permissionService.findByProject(projectId);
    }

    @DeleteMapping("/{id}/project/{projectId}")
    public void delete(@PathVariable Long id){
        permissionService.delete(id);
    }

}
