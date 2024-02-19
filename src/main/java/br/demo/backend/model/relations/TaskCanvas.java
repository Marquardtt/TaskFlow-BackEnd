package br.demo.backend.model.relations;

import br.demo.backend.model.ids.TaskPageId;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_task_canvas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskCanvas extends TaskPage {

    //Patch
    private Double x = 100.0;
    //Patch
    private Double y = 100.0;

    public TaskCanvas(Long id, Task task, Double x, Double y){
        super(id, task);
        this.x = x;
        this.y = y;
    }

}
