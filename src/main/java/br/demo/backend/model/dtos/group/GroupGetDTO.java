package br.demo.backend.model.dtos.group;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Archive picture;
    private String description;
    private Collection<PermissionGetDTO> permissions;
    private SimpleUserGetDTO owner;
    private Collection<SimpleUserGetDTO> users;

}