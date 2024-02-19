package br.demo.backend.model.dtos.properties;

import br.demo.backend.model.properties.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SelectGetDTO extends PropertyGetDTO {
    //Patch
    private Collection<Option> options;

}
