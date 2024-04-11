package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class OtherUsersToDTO implements ModelToGetDTO<User, OtherUsersDTO> {
    @Override
    public OtherUsersDTO tranform(User user) {
        if(user == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(user, simpleUser);
//        System.out.println(simpleUser);
        simpleUser.setUsername(user.getUserDetailsEntity().getUsername());
        return simpleUser;
    }
}
