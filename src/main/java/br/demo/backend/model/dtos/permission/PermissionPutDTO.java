package br.demo.backend.model.dtos.permission;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypePermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionPutDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private TypePermission permission = TypePermission.READ;
    private Boolean isDefault;

}
