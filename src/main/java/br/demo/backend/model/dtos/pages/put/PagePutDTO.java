package br.demo.backend.model.dtos.pages.put;


import br.demo.backend.model.dtos.project.ProjectGetDTO;
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
public class PagePutDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
}
