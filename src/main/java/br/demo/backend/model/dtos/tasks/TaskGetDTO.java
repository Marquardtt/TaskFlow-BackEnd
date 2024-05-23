package br.demo.backend.model.dtos.tasks;


import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    private Boolean deleted;
    private Boolean completed;
    private Boolean waitingRevision;

    private Collection<PropertyValueGetDTO> properties;

    private Collection<LogGetDTO> logs;


    private Collection<MessageGetDTO> comments;

}
