package br.demo.backend.model.pages;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagePostDTO {

    private String name;
    private TypeOfPage type ;
    private Project project;

}
