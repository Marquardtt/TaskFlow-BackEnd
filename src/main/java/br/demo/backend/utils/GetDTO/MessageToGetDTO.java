package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class MessageToGetDTO implements ModelToGetDTO<Message, MessageGetDTO> {

    private final DestinationToGetDTO destinationToGetDTO;

    private final OtherUsersToDTO otherUsersToDTO;

    public MessageToGetDTO(DestinationToGetDTO destinationToGetDTO,
                           OtherUsersToDTO otherUsersToDTO) {
        this.destinationToGetDTO = destinationToGetDTO;
        this.otherUsersToDTO = otherUsersToDTO;
    }

    @Override
    public  MessageGetDTO tranform(Message message) {
        if(message == null) return null;
        MessageGetDTO messageGet = new MessageGetDTO();
        BeanUtils.copyProperties(message, messageGet);
        messageGet.setSender(otherUsersToDTO.tranform(message.getSender()));
        try {
            messageGet.setDestinations(message.getDestinations().stream().map(destinationToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        return messageGet;
    }

}
