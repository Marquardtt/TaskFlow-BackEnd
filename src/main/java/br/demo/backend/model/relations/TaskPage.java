package br.demo.backend.model.relations;

import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.relations.ids.TaskPageId;
import br.demo.backend.model.pages.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class TaskPage {

    @Id
    private Long taskId;
    @Id
    private Long pageId;

    @ManyToOne
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    private Task task;
    @ManyToOne
    @JoinColumn(name = "pageId", insertable = false, updatable = false)
    private Page page;
    private Double x;
    private Double y;
}
