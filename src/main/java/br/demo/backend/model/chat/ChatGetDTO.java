package br.demo.backend.model.chat;

import br.demo.backend.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGetDTO {
    private Long id;
    private String picture;
    private Integer quantitityUnvisualized;
    private Message lastMessage;
    private String name;
}
