package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.dtos.chat.get.ChatPrivateGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class ChatPrivateToGetDTO implements ModelToGetDTO<ChatPrivate, ChatPrivateGetDTO> {
    @Override
    public ChatPrivateGetDTO tranform(ChatPrivate chatPrivate) {
        if(chatPrivate == null) return null;
        ChatPrivateGetDTO chatGet = new ChatPrivateGetDTO();
        BeanUtils.copyProperties(chatPrivate, chatGet);
        try {
            chatGet.setUsers(chatPrivate.getUsers().stream().map(ChatPrivateToGetDTO::tranformSimple).toList());
        } catch (NullPointerException ignore) {}
        return chatGet;
    }

    private static OtherUsersDTO tranformSimple(User obj){
        if(obj == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(obj, simpleUser);
        System.out.println(simpleUser);
        simpleUser.setUsername(obj.getUserDetailsEntity().getUsername());
        return simpleUser;
    }
}
