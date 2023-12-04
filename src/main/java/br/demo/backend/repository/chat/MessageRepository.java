package br.demo.backend.repository.chat;

import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository

public interface MessageRepository extends JpaRepository<Message, Long> {


}