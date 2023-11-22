package br.demo.backend.service;


import br.demo.backend.model.Message;
import br.demo.backend.repository.MessageRepository;
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
