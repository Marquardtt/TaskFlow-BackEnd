package br.demo.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@Table(name = "tb_task")
@NoArgsConstructor

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    @OneToMany
    private Collection<Property> properties;

    private Boolean deleted;
    @OneToMany
    Collection<Log> logs;
    @OneToMany
    Collection<Message> comments;

}
