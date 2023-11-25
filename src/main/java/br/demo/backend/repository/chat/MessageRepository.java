package br.demo.backend.repository.chat;

import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public Collection<Message> getAllByChatAndVisualized(Chat c, Boolean v);

}