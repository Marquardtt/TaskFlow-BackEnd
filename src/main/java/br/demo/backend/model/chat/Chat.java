package br.demo.backend.model.chat;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_chat")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    //Patch
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> messages;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, updatable = false)
    private TypeOfChat type;

    @OneToOne
    //Patch
    private Message lastMessage;

    public abstract  Collection<User> finUsers();
}
