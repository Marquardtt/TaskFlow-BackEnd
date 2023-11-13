package br.demo.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "task")
    private Collection<TaskPropertiesModel> properties;

}
