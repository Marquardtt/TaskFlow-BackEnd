package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_task_page")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TaskPageId.class)
public class TaskPageModel {

    @Id
    private Long taskId;
    @Id
    private Long pageId;

    @ManyToOne
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    private TaskModel task;
    @ManyToOne
    @JoinColumn(name = "pageId", insertable = false, updatable = false)
    private PageModel page;
    private Double x;
    private Double y;
}
