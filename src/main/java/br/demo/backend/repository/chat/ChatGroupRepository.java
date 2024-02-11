package br.demo.backend.repository.chat;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.enums.TypeOfChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    public Collection<ChatGroup> findChatsByGroup_UsersContainingOrderByMessagesDateTimeDesc(User user);


}
