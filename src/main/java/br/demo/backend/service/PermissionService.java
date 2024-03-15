package br.demo.backend.service;


import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.permission.PermissionPostDTO;
import br.demo.backend.model.dtos.permission.PermissionPutDTO;
import br.demo.backend.model.enums.TypePermission;
import br.demo.backend.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PermissionService {

    private PermissionRepository permissionRepository;
    private AutoMapper<Permission> autoMapper;


    public Collection<PermissionGetDTO> findAll() {
        return permissionRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();

    }

    public PermissionGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(permissionRepository.findById(id).get());
    }

    public void save(PermissionPostDTO permissionDto) {
        if(permissionDto.getPermission() == null){
            permissionDto.setPermission(TypePermission.READ);
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDto, permission);
        permissionRepository.save(permission);
    }

    public void update(PermissionPutDTO permissionDto, Boolean patching) {
        if(permissionDto.getPermission() == null){
            permissionDto.setPermission(TypePermission.READ);
        }
        Permission oldPermission = permissionRepository.findById(permissionDto.getId()).get();
        Permission permission = patching ? oldPermission : new Permission();
        autoMapper.map(permissionDto, permission, patching);
        permission.setProject(oldPermission.getProject());
        permissionRepository.save(permission);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
