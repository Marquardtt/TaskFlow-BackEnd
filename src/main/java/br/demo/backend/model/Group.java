package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    //Patch
    private String picture;
    private String description;

    @ManyToMany
    private Collection<Permission> permission;
    @ManyToOne
    private User owner;
    @ManyToMany
    private Collection<User> users;

}