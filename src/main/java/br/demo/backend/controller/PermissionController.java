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

    //Precisa ser o owner do projeto da permission
    @PostMapping
    public void insert(@RequestBody PermissionPostDTO permission){
        permissionService.save(permission);
    }

    //Precisa ser o owner do projeto da permission
    @PutMapping
    public void upDate(@RequestBody PermissionPutDTO permission){
        permissionService.update(permission, false);
    }
    //Precisa ser o owner do projeto da permission
    @PatchMapping
    public void patch(@RequestBody PermissionPutDTO permission){
        permissionService.update(permission, true);
    }

    //Precisa ser o owner do projeto da permission
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        permissionService.delete(id);
    }

}
