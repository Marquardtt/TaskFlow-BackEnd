package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_task_property")
public class TaskPropertiesModel {
    //Dar uma revisada depois
    private String value;
}

