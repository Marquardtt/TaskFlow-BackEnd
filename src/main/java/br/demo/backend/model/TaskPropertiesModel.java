package br.demo.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.config.Task;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_task_property")
@IdClass(TaskPropertiesId.class)
public class TaskPropertiesModel {
    //Dar uma revisada

    @Id
    private Long taskId;
    @Id
    private Long propertyId;
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskId")
    private TaskModel task;
    @ManyToOne
    @JoinColumn(name = "propertyId")
    private GroupModel group;
}

