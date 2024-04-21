package br.demo.backend.model.dtos.permission;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypePermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionPostDTO {

    private String name;
    private TypePermission permission = TypePermission.READ;
    private Project project;
    private Boolean isDefault;
}
