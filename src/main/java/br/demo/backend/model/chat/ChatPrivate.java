package br.demo.backend.model.chat;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfChat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_chat_private")
public class ChatPrivate extends Chat  {

    //Always two
    @ManyToMany
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Collection<User> users;

    @Override
    public Collection<User> finUsers() {
        return users;
    }
}