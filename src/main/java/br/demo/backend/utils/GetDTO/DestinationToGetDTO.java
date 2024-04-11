package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.Destination;
import br.demo.backend.model.dtos.chat.get.DestinationGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class DestinationToGetDTO implements ModelToGetDTO<Destination, DestinationGetDTO> {
    @Override
    public DestinationGetDTO tranform(Destination destination) {
        if(destination == null) return null;
        DestinationGetDTO destinationGet = new DestinationGetDTO();
        BeanUtils.copyProperties(destination, destinationGet);
        destinationGet.setUser(tranformSimple(destination.getUser()));
        return destinationGet;
    }

    private static OtherUsersDTO tranformSimple(User obj){
        if(obj == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(obj, simpleUser);
//        System.out.println(simpleUser);
        simpleUser.setUsername(obj.getUserDetailsEntity().getUsername());
        return simpleUser;
    }
}
