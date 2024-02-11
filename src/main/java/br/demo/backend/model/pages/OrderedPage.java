package br.demo.backend.model.pages;


import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
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

//KANBAN, TIMELINE, LIST, TABLE
public class OrderedPage extends Page {
    //Patch
    @ManyToOne
    private Property propertyOrdering;

    //Cuidado para nao deletar as tasks sem querer
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<TaskPage> tasks;

}
