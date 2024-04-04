package br.demo.backend.service;


import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
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
    private ProjectRepository projectRepository;

    public PermissionGetDTO save(PermissionPostDTO permissionDto) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDto, permission);
        return ModelToGetDTO.tranform(permissionRepository.save(permission));
    }

    public PermissionGetDTO update(PermissionPutDTO permissionDto, Boolean patching) {
        Permission oldPermission = permissionRepository.findById(permissionDto.getId()).get();
        Permission permission = patching ? oldPermission : new Permission();
        autoMapper.map(permissionDto, permission, patching);
        //keep the project of the permission
        permission.setProject(oldPermission.getProject());
        return ModelToGetDTO.tranform(permissionRepository.save(permission));
    }

    //find the permissions of a project
    public Collection<PermissionGetDTO> findByProject(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        return permissionRepository.findByProject(project).stream().map(ModelToGetDTO::tranform).toList();
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
