package br.demo.backend.service.chat;


import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import br.demo.backend.repository.chat.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MessageService {

    MessageRepository messageRepository;

    public Collection<Message> findAll() {
        return messageRepository.findAll();
    }
    public Collection<Message> findAllByChat(Long id) {
        return messageRepository.findAllByChat_Id(id);
    }
    public Message findOne(Long id) {
        return messageRepository.findById(id).get();
    }

    public void save(Message message) {
        messageRepository.save(message);
    }
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}
