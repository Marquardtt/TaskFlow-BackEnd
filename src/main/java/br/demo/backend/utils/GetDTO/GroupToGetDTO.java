package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.utils.Implementacoes;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class GroupToGetDTO implements ModelToGetDTO<Group, GroupGetDTO> {
    private final PermissionToGetDTO permissionToGetDTO;
    private final OtherUsersToDTO otherUsersToDTO;

    public GroupToGetDTO(PermissionToGetDTO permissionToGetDTO, OtherUsersToDTO otherUsersToDTO) {
        this.permissionToGetDTO = permissionToGetDTO;
        this.otherUsersToDTO = otherUsersToDTO;
    }
    @Override
    public GroupGetDTO tranform(Group group) {
        if(group == null) return null;
        GroupGetDTO groupGet = new GroupGetDTO();
        BeanUtils.copyProperties(group, groupGet);
        groupGet.setOwner(otherUsersToDTO.tranform(group.getOwner()));
        try {
            groupGet.setPermissions(group.getPermissions().stream().map(permissionToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        try {
            groupGet.setUsers(group.getUsers().stream().map(otherUsersToDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        return groupGet;
    }
}
