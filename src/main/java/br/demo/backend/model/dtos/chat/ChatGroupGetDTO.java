package br.demo.backend.model.dtos.chat;

import br.demo.backend.model.Group;
import br.demo.backend.model.chat.Chat;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatGroupGetDTO extends ChatGetDTO {

    private Group group;
}