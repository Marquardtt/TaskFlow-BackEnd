package br.demo.backend.model.dtos.relations;

import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.values.DeserializerValue;
import br.demo.backend.model.values.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonDeserialize(using = DeserializerValue.class)

public class PropertyValueGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private PropertyGetDTO property;
    private Value value;
}