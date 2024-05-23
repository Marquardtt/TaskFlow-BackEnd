package br.demo.backend.model.dtos.permission;

import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.model.enums.TypePermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private TypePermission permission;
    private SimpleProjectGetDTO project;
    private Boolean isDefault;
}
