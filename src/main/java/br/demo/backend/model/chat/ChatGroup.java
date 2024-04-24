package br.demo.backend.model.chat;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_chat_group")
public class ChatGroup extends Chat {

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Group group;

    @Override
    public Collection<User> finUsers() {
        ArrayList<User> users =  new ArrayList<>(group.getUsers());
        users.add(this.group.getOwner());
        return users;
    }
}