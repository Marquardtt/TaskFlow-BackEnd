package br.demo.backend.model;


import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE)
    private Collection<Multivalued> multiProperties;
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE)
    private Collection<Univalued> uniProperties;
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE)
    private Collection<UserValue> userProperties;

    private Boolean deleted;
    @OneToMany
    Collection<Log> logs;
    @OneToMany
    Collection<Message> comments;



}
