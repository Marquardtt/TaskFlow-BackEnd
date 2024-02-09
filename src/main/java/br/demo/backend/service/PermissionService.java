package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.model.Permission;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PermissionService {

    private PermissionRepository permissionRepository;
    private AutoMapper<Permission> autoMapper;


    public Collection<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findOne(Long id) {
        return permissionRepository.findById(id).get();
    }

    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

    public void update(Permission permissionDto, Boolean patching) {
        Permission permission = patching ? permissionRepository.findById(permissionDto.getId()).get() : new Permission();
        autoMapper.map(permissionDto, permission, patching);
        permissionRepository.save(permission);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
