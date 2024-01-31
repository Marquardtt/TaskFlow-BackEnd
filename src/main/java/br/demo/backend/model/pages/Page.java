package br.demo.backend.model.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.relations.TaskPage;
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
@Inheritance(strategy = InheritanceType.JOINED)
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private TypeOfPage type;

    //Cuidado para nao deletar as tasks sem querer
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<TaskPage> tasks;

    @ManyToMany(mappedBy = "pages")
    private Collection<Property> properties;

    @ManyToOne
    private Project project;

    public Page(Long id){
        this.id = id;
    }
}
