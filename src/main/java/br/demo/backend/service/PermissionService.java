package br.demo.backend.service;


import br.demo.backend.model.Permission;
import br.demo.backend.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PermissionService {

    PermissionRepository permissionRepository;

    public Collection<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findOne(Long id) {
        return permissionRepository.findById(id).get();
    }

    public void save(Permission permission) {
        permissionRepository.save(permission);
    }
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
