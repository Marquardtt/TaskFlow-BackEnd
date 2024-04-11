package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class PermissionToGetDTO implements ModelToGetDTO<Permission, PermissionGetDTO> {

    private final ProjectToGetDTO projectToGetDTO;

    public PermissionToGetDTO(ProjectToGetDTO projectToGetDTO) {
        this.projectToGetDTO = projectToGetDTO;
    }

    @Override
    public PermissionGetDTO tranform(Permission permission) {
        if(permission == null) return null;
        PermissionGetDTO permissionGet = new PermissionGetDTO();
        BeanUtils.copyProperties(permission, permissionGet);
        permissionGet.setProject(projectToGetDTO.tranformSimple(permission.getProject()));
        return permissionGet;
    }
}
