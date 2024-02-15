package br.demo.backend.model.dtos.chat.post;

import br.demo.backend.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupPostDTO {

    private Group group;
}