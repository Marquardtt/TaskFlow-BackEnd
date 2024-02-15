package br.demo.backend.model.dtos.pages.post;


import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.enums.TypeOfPage;
import lombok.*;

import java.util.Collection;


@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
//CALENDAR
public class PagePostDTO {
    private String name;

    private TypeOfPage type;

    private ProjectGetDTO project;

}
