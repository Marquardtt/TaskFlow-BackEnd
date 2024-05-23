package br.demo.backend.repository.chat;
import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.ChatGroup;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    public Collection<ChatGroup> findAllByGroup_OwnerOrGroup_UsersContainingOrderByLastMessage_DateCreateDesc(User user, User user2);
    public Collection<ChatGroup> findAllByGroup_UsersContaining( User user);

    public ChatGroup findByGroup(@NotNull Group group);

    Boolean existsByGroup_Id(Long id);
}
