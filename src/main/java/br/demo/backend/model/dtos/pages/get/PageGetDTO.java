package br.demo.backend.model.dtos.pages.get;


import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.enums.TypeOfPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
//CALENDAR
public class PageGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    private TypeOfPage type;

    private Collection<PropertyGetDTO> properties;

    private Collection<TaskPageGetDTO> tasks;

}
