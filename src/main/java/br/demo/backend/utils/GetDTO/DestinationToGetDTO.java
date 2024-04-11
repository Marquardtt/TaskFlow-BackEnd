package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.chat.Destination;
import br.demo.backend.model.dtos.chat.get.DestinationGetDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class DestinationToGetDTO implements ModelToGetDTO<Destination, DestinationGetDTO> {
    private final OtherUsersToDTO otherUsersToDTO;

    public DestinationToGetDTO(OtherUsersToDTO otherUsersToDTO) {
        this.otherUsersToDTO = otherUsersToDTO;
    }

    @Override
    public DestinationGetDTO tranform(Destination destination) {
        if(destination == null) return null;
        DestinationGetDTO destinationGet = new DestinationGetDTO();
        BeanUtils.copyProperties(destination, destinationGet);
        destinationGet.setUser(otherUsersToDTO.tranform(destination.getUser()));
        return destinationGet;
    }


}
