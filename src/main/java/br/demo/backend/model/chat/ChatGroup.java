package br.demo.backend.model.chat;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
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
@Table(name = "tb_chat_group")
public class ChatGroup extends Chat {

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Group group;
}