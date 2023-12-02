package br.demo.backend.model;

import br.demo.backend.model.relations.UserGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String picture;
    private String description;

    @ManyToMany
    private Collection<UserGroup> users;

}