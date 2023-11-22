package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

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
    @OneToMany
    private Collection<Page> pages;
    @OneToMany
    private Collection<Property> properties;
}