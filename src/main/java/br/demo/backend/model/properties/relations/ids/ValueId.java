package br.demo.backend.model.properties.relations.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueId implements Serializable {
    private Long propertyId;
    private Long taskId;
}
