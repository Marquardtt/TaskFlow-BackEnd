package br.demo.backend.model.chat;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfChat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private Collection<User> users;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> messages;
    @Enumerated(EnumType.STRING)
    private TypeOfChat type;
    private String name;
    private String picture;
    private Integer quantitityUnvisualized;
    @OneToOne
    private Message lastMessage;


}