package br.demo.backend.repository.chat;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    public Collection<ChatGroup> findChatsByGroup_Users_UserDetailsEntity_UsernameOrderByLastMessage_DateCreateDesc(String username);
    public Collection<ChatGroup> findAllByGroup_UsersContaining( User user);


}
