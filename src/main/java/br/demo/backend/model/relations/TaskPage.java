package br.demo.backend.model.relations;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

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
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Task task;
}