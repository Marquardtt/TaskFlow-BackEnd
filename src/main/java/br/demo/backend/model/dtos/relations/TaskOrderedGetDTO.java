package br.demo.backend.model.dtos.relations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskOrderedGetDTO extends TaskPageGetDTO {

    private Integer indexAtColumn = 0;
}
