package br.demo.backend.service.chat;


import br.demo.backend.model.chat.Chat;
import br.demo.backend.repository.chat.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ChatService {

    ChatRepository chatRepository;

    public Collection<Chat> findAll() {
        return chatRepository.findAll();
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
