package br.demo.backend.model.dtos.relations;

import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.values.DeserializerValue;
import br.demo.backend.model.values.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonDeserialize(using = DeserializerValue.class)

public class TaskValueGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private PropertyGetDTO property;
    private Value value;
}