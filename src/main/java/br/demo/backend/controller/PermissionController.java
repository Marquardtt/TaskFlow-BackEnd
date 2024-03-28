package br.demo.backend.controller;


import br.demo.backend.model.Permission;
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

    //FEITO
    @PostMapping
    public PermissionGetDTO insert(@RequestBody PermissionPostDTO permission){
        return permissionService.save(permission);
    }

    //FEITO
    @PutMapping
    public PermissionGetDTO upDate(@RequestBody PermissionPutDTO permission){
        return permissionService.update(permission, false);
    }
    //FEITO
    @PatchMapping
    public PermissionGetDTO patch(@RequestBody PermissionPutDTO permission){
        return permissionService.update(permission, true);
    }

    //FEITO
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        permissionService.delete(id);
    }

}
