package br.demo.backend.model.dtos.chat.get;

import br.demo.backend.model.Archive;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfChat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class ChatGetDTO {

    private Long id;
    private Collection<MessageGetDTO> messages;
    private TypeOfChat type;
    private MessageGetDTO lastMessage;
    private Integer quantityUnvisualized;
    private String name;
    private Archive picture;
}
