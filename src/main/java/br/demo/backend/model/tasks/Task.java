package br.demo.backend.model.tasks;


import br.demo.backend.model.chat.Message;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.relations.PropertyValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@Table(name = "tb_task")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Task implements ILogged {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Patch
    private String name;


    private OffsetDateTime dateDeleted;
    //Patch
    private Boolean deleted = false;
    private OffsetDateTime dateCompleted;
    //Patch
    private Boolean completed = false;
    private Boolean waitingRevision = false;

    //Patch
    @JoinColumn(name = "task_id")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<PropertyValue> properties;


    @JoinColumn(name = "task_id")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Log> logs;

    //Patch
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> comments;
    public Task(Long id){
        this.id= id;
    }

    @Override
    public Collection<PropertyValue> getPropertiesValues() {
        return this.properties;
    }
}
