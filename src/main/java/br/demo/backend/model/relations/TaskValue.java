package br.demo.backend.model.relations;

import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.DeserializerValue;
import br.demo.backend.model.values.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_task_value")
@JsonDeserialize(using = DeserializerValue.class)
public class TaskValue {
    // TODO: 13/02/2024 Id Composto SE não for usado em project tambem

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Property property;
    
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Value value;
}
