package br.demo.backend.service.chat;


import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.repository.chat.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;

    public Collection<Chat> findAllPrivate(Long id) {
        return chatRepository.findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(id), TypeOfChat.PRIVATE);
    }

    public Collection<Chat> findAllGroup(Long id) {
        return chatRepository.findChatsByUsersContainingAndTypeOrderByMessagesDateTimeDesc(new User(id), TypeOfChat.GROUP);
    }


    public void updateMessagesToVisualized(Chat chat) {
        for (Message m : chat.getMessages()) {
            m.setVisualized(true);
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
        chatRepository.save(chat);
    }

    public void delete(Long id) {
        chatRepository.deleteById(id);
    }
}
