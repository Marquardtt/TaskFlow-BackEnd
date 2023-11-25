package br.demo.backend.service.chat;


import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGetDTO;
import br.demo.backend.model.chat.Message;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.repository.chat.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private MessageRepository messageRepository;
    private GroupRepository groupRepository;

    public Collection<ChatGetDTO> findAllPrivaty() {
        Collection<Chat> chats = chatRepository.findChatsByOrderByMessagesDateTimeDesc();
        Collection<ChatGetDTO> newChats = new HashSet<>();

        for(Chat chat : chats){
            ChatGetDTO newChat = new ChatGetDTO();
            BeanUtils.copyProperties(chat, newChat);
            ArrayList<Message> messages = new ArrayList<>(chat.getMessages());
            newChat.setLastMessage(messages.get(messages.size()-1));
            newChat.setQuantitityUnvisualized(
                    messageRepository.getAllByChatAndVisualized(chat, false).size()
            );
            if(chat.getUsers().size() == 1){
                User user = new ArrayList<User>(chat.getUsers()).get(0);
                newChat.setPicture(user.getPicture());
                newChat.setName(user.getName() +  " " + user.getSurname());
            }else{
                Group group = groupRepository.findGroupByUsers(chat.getUsers());
                newChat.setPicture(group.getPicture());
                newChat.setName(group.getName());
            }
            newChats.add(newChat);
        }
        return newChats;
    }

    public Collection<ChatGetDTO> findAllGroup(){
    return null;
    }


    public void updateMessagesToVisualized(Chat chat){
        for(Message m : chat.getMessages()){
            m.setVisualized(true);
        }
        chatRepository.save(chat);
    }


    public Chat findOne(Long id) {
        return chatRepository.findById(id).get();
    }

    public void save(Chat chatModel) {
        chatRepository.save(chatModel);
    }

    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
