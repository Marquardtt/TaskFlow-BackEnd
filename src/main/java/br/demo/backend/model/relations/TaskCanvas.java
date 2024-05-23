package br.demo.backend.model.relations;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(nullable = false)
    private Double x = 500.0;
    //Patch
    @NotNull
    @Column(nullable = false)
    private Double y = 500.0;

    public TaskCanvas(Long id, Task task, Double x, Double y){
        super(id, task);
        this.x = x;
        this.y = y;
    }

}
