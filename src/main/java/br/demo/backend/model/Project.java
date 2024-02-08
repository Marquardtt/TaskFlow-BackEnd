package br.demo.backend.model;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "tb_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    //Patch
    private String picture;
    private LocalDate deadline;
    //Patch
    private LocalDateTime visualizedAt;
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "project")
    private Collection<Page> pages;
    @OneToMany(mappedBy = "project")
    private Collection<Property> properties;

    public Project(Long id){
        this.id = id;
    }
}