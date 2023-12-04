package br.demo.backend.model;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String picture;

    LocalDateTime visualizedAt;
    @ManyToOne
    private User owner;
    private Boolean gamification;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Page> pages;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Property> properties;

    public Project(Long id){
        this.id = id;
    }
}