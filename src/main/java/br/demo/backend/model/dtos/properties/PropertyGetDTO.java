package br.demo.backend.model.dtos.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropertyGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Boolean visible = true;
    private Boolean obligatory = false;
    private TypeOfProperty type;
}
