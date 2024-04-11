package br.demo.backend.model.dtos.pages.post;


import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import lombok.*;


@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
//CALENDAR
public class PagePostDTO {
    private String name;

    private TypeOfPage type;

    private Project project;

}
