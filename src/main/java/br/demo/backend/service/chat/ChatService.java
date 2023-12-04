package br.demo.backend.service.chat;


import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.chat.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;

    public Collection<Chat> findAllPrivate(Long userId) {
        Collection<Chat> chats = chatRepository.findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(userId), TypeOfChat.PRIVATE);
        setQuantityUnvisualized(chats, userId);
        return chats;
    }

    private void setQuantityUnvisualized(Collection<Chat> chats, Long userId){
        for (Chat c : chats) {
            Integer qttyUnvisualized = 0;
            for(Message m : c.getMessages()){
                if(!m.getVisualized() && !m.getUser().getId().equals(userId)){
                    qttyUnvisualized++;
                }
            }
            c.setQuantitityUnvisualized(qttyUnvisualized);
        }
    }

    public Collection<Chat> findAllGroup(Long userId) {
        Collection<Chat> chats = chatRepository.findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(userId), TypeOfChat.GROUP);
        setQuantityUnvisualized(chats, userId);
        return chats;
    }


    public void updateMessagesToVisualized(Chat chat, Long userId) {
        for (Message m : chat.getMessages()) {
            if(!m.getUser().getId().equals(userId)){
                m.setVisualized(true);
            }
        }
        chatRepository.save(chat);
    }

    public Collection<Chat> findByName(String name) {
        return chatRepository.findChatsByNameContains(name);
    }

    public Chat findOne(Long id) {
        return chatRepository.findById(id).get();
    }

    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public void update(Chat chat) {
        HashSet<Message> messages = new HashSet<>(chat.getMessages());
        chat.setLastMessage(messages.stream().max((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime())).get());
        chatRepository.save(chat);
    }

    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
