package br.demo.backend.model.dtos.chat.get;

import br.demo.backend.model.Group;
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