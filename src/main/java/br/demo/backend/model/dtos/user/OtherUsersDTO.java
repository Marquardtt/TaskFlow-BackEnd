package br.demo.backend.model.dtos.user;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Configuration;
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
    private String id;
    private String name;
    private String surname;
    private String mail;
    private Integer points;
    private String description;
}