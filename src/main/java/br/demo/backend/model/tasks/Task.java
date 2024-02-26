package br.demo.backend.model.tasks;


import br.demo.backend.model.chat.Message;
import br.demo.backend.model.relations.TaskValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@Table(name = "tb_task")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Task {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Patch
    private String name;


    //Patch
    private Boolean deleted = false;
    //Patch
    private Boolean completed = false;

    //Patch
    @JoinColumn(name = "task_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<TaskValue> properties;


    @JoinColumn(name = "task_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Log> logs;

    //Patch
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Message> comments;
    public Task(Long id){
        this.id= id;
    }

}
