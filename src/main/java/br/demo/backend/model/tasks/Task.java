package br.demo.backend.model.tasks;


import br.demo.backend.model.chat.Message;
import br.demo.backend.model.relations.TaskValue;
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
    private Boolean deleted;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<TaskValue> properties;

    @OneToMany(cascade = CascadeType.ALL)
    Collection<Log> logs;

    @OneToMany(cascade = CascadeType.ALL)
    Collection<Message> comments;
    public Task(Long id){
        this.id= id;
    }

}
