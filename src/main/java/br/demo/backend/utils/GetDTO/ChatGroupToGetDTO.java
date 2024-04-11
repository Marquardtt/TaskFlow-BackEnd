package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class ChatGroupToGetDTO implements ModelToGetDTO<ChatGroup, ChatGroupGetDTO> {

    private final GroupToGetDTO groupToGetDTO;

    public ChatGroupToGetDTO(GroupToGetDTO groupToGetDTO) {
        this.groupToGetDTO = groupToGetDTO;
    }

    @Override
    public ChatGroupGetDTO tranform(ChatGroup chatGroup) {
        if(chatGroup == null) return null;
        ChatGroupGetDTO chatGet = new ChatGroupGetDTO();
        BeanUtils.copyProperties(chatGroup, chatGet);
        chatGet.setGroup(groupToGetDTO.tranform(chatGroup.getGroup()));
        return chatGet;
    }
}
