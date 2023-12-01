package br.demo.backend.model.relations;

import br.demo.backend.model.properties.Property;
import br.demo.backend.model.values.Value;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_task_value")
public class TaskValue {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Property property;

    @ManyToOne
    private Value value;
}
