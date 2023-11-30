package br.demo.backend.model;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Collection;
import java.util.Date;

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
    private Date date;
    private String picture;

    @OneToMany
    private Collection<Group> groups;
    @ManyToOne
    private User owner;
    @OneToMany
    private Collection<Page> pages;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Property> properties;
}