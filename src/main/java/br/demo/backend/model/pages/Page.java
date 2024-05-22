package br.demo.backend.model.pages;


import br.demo.backend.model.Project;
import br.demo.backend.interfaces.IHasProperties;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.relations.TaskPage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "tb_page")
@Inheritance(strategy = InheritanceType.JOINED)
//CALENDAR
public class Page implements IHasProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    @Column(nullable = false, updatable = false)
    private TypeOfPage type;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id")
    private Collection<TaskPage> tasks;


    @ManyToMany(mappedBy = "pages", cascade = CascadeType.REMOVE)
    private Collection<Property> properties;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Project project;


    public Page(Long id) {
        this.id = id;
    }
}
