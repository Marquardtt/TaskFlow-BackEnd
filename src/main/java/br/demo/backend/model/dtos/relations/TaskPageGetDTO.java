package br.demo.backend.model.dtos.relations;

import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.relations.DeserializerTaskPage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonDeserialize(using = DeserializerTaskPage.class)
public class TaskPageGetDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private TaskGetDTO task;
}