package br.demo.backend.model.relations;

import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_task_ordered")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskOrdered extends TaskPage {

    private Integer indexAtColumn = 0;

    public TaskOrdered(Long id, Task task, Integer indexAtColumn){
        super(id, task);
        this.indexAtColumn = indexAtColumn;
    }
}
