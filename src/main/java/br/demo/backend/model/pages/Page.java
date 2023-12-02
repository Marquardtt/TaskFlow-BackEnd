package br.demo.backend.model.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.enums.TypeOfPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_page")

public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private TypeOfPage type ;
    @OneToMany
    private Collection<TaskPage> tasks;
    @ManyToMany
    private Collection<Property> properties;
    @ManyToOne
    private Property propertyOrdering;

    public Page(Long id){
        this.id = id;
    }
}
