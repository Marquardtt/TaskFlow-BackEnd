package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPutDTO {
    private Long id;
    private String name;
    private Boolean visible;
    private Boolean obligatory;
    private TypeOfProperty type;
    private Page page;
    private Project project;
}
