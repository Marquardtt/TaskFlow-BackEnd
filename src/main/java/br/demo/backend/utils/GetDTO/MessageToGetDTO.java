package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class MessageToGetDTO implements ModelToGetDTO<Message, MessageGetDTO> {

    private final DestinationToGetDTO destinationToGetDTO;

    public MessageToGetDTO(DestinationToGetDTO destinationToGetDTO) {
        this.destinationToGetDTO = destinationToGetDTO;
    }

    @Override
    public  MessageGetDTO tranform(Message message) {
        if(message == null) return null;
        MessageGetDTO messageGet = new MessageGetDTO();
        BeanUtils.copyProperties(message, messageGet);
        messageGet.setSender(tranformSimple(message.getSender()));
        try {
            messageGet.setDestinations(message.getDestinations().stream().map(destinationToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        return messageGet;
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
