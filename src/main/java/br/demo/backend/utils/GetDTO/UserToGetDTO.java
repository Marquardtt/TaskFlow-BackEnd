package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.utils.Implementacoes;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class UserToGetDTO implements ModelToGetDTO<User, UserGetDTO> {

    private final PermissionToGetDTO permissionToGetDTO;

    public UserToGetDTO(PermissionToGetDTO permissionToGetDTO) {
        this.permissionToGetDTO = permissionToGetDTO;
    }

    @Override
    public UserGetDTO tranform(User user) {
        UserGetDTO userGet = new UserGetDTO();
        BeanUtils.copyProperties(user, userGet);
        userGet.setUsername(user.getUserDetailsEntity().getUsername());
        try {
            userGet.setPermissions(user.getPermissions().stream().map(permissionToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        return userGet;
    }
}
