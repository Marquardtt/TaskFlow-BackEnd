package br.demo.backend.model.dtos.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LimitedGetDTO extends PropertyGetDTO {
    private Integer maximum;
}
