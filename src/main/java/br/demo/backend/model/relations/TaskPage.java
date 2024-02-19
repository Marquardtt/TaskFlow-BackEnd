package br.demo.backend.model.relations;

import br.demo.backend.model.ids.TaskPageId;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.DeserializerValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_task_page")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@JsonDeserialize(using = DeserializerTaskPage.class)
public class TaskPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne
    private Task task;
}