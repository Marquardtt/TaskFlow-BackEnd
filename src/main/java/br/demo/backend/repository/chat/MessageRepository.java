package br.demo.backend.repository.chat;

import br.demo.backend.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}