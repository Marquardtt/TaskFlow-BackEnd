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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Property property;

    @OneToOne(cascade = CascadeType.ALL)
    private Value value;
}
