package br.demo.backend.model.dtos.chat;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatPrivateGetDTO extends ChatGetDTO {
    private Collection<User> users;
}