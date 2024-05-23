package br.demo.backend.model.dtos.user;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OtherUsersDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String mail;
    private String phone;
    private Integer points;
    private String description;
    private Archive picture;
    private boolean authenticate;
    private Collection<PermissionGetDTO> permissions;
}