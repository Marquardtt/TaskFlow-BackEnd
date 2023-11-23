package br.demo.backend.model.properties.relations;

import br.demo.backend.model.Property;
import br.demo.backend.model.Task;
import br.demo.backend.model.properties.relations.ids.ValueId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_univalued")
@IdClass(ValueId.class)

public class Univalued{

    @Id
    private Long taskId;
    @Id
    private Long propertyId;

    private String value;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    private Task task;
    @ManyToOne
    @JoinColumn(name = "propertyId", insertable = false, updatable = false)
    private Property property;


}

