package br.demo.backend.repository.chat;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.ChatPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatPrivateRepository extends JpaRepository<ChatPrivate, Long> {

    public Collection<ChatPrivate> findChatsByUsers_UserDetailsEntity_UsernameOrderByLastMessage_DateCreateDesc(String username);

    public Collection<ChatPrivate> findAllByUsersContaining(User user);

}
