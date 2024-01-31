package br.demo.backend.model.relations;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "tb_task_canvas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Task task;

    //Patch
    private Double x;
    //Patch
    private Double y;
    //Patch
    private Integer indexAtColumn;
}
