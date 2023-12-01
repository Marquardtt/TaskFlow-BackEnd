package br.demo.backend.model.values;

import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_value")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Value {
    @Id
    @GeneratedValue
    private Long id;
}
