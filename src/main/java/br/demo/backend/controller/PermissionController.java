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

    @GetMapping("/{id}/project/{projectId}")
    public PermissionGetDTO findOne(@PathVariable Long id){
        return permissionService.findOne(id);
    }

    @GetMapping("/project/{projectId}")
    public Collection<PermissionGetDTO> findAll(){
        return permissionService.findAll();
    }

    @DeleteMapping("/{id}/project/{projectId}")
    public void delete(@PathVariable Long id){
        permissionService.delete(id);
    }

}
