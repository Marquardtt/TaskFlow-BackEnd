package br.demo.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_task_page")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPageModel {

    @Id
    private Long id;
    private Double x;
    private Double y;
}
