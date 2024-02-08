package br.demo.backend.model.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table(name = "db_common_page")
public class CommonPage extends Page {
    //Patch
    @ManyToOne
    private Property propertyOrdering;

}
