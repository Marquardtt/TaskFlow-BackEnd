package br.demo.backend.model.dtos.chat.post;

import br.demo.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPrivatePostDTO{
    private Collection<User> users;
}