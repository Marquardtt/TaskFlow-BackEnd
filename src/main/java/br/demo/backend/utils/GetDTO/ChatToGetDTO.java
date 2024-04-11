package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.dtos.chat.get.ChatGetDTO;
import br.demo.backend.model.dtos.chat.get.ChatGroupGetDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ChatToGetDTO implements ModelToGetDTO<Chat, ChatGetDTO> {
    private final MessageToGetDTO messageToGetDTO;
    private final ChatGroupToGetDTO chatGroupToGetDTO;
    private final ChatPrivateToGetDTO chatPrivateToGetDTO;

    public ChatToGetDTO(MessageToGetDTO messageToGetDTO,
                        ChatGroupToGetDTO chatGroupToGetDTO,
                        ChatPrivateToGetDTO chatPrivateToGetDTO) {
        this.messageToGetDTO = messageToGetDTO;
        this.chatGroupToGetDTO = chatGroupToGetDTO;
        this.chatPrivateToGetDTO = chatPrivateToGetDTO;
    }

    @Override
    public ChatGetDTO tranform(Chat chat) {
        if(chat == null) return null;
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Integer qttyUnvisualized = chat.getMessages().stream().filter(m ->
                m.getDestinations().stream().anyMatch(d ->
                        d.getUser().getUserDetailsEntity().getUsername().equals(username) && !d.getVisualized()
                )).toList().size();
        ChatGetDTO chatGet = chat.getType().equals(TypeOfChat.GROUP)  ?
                chatGroupToGetDTO.tranform((ChatGroup) chat): chatPrivateToGetDTO.tranform((ChatPrivate) chat);

        chatGet.setQuantityUnvisualized(qttyUnvisualized);
        try {
            chatGet.setMessages(chat.getMessages().stream().map(messageToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
//        chat.setLastMessage(messageToGetDTO.tranform(chat.getLastMessage()));
        return chatGet;
    }

}
