package br.demo.backend.model;

import br.demo.backend.model.relations.UserGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String address;
    private String picture;
    private String mail;
    private String phone;
    private String description;
    private Integer points;
    @OneToOne(cascade = CascadeType.ALL)
    private Configuration configuration;
    @ManyToMany
    private Collection<UserGroup> groups;
    @OneToMany
    private Collection<Project> project;
    public User (Long id){
        this.id = id;
    }
}