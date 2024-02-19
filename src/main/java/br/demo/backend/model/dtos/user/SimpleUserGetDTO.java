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
public class SimpleUserGetDTO {

    @EqualsAndHashCode.Include
    private String username;
    private String name;
    private String surname;
    private String address;
    private Archive picture;
    private String mail;
    private String phone;
    private Integer points;
    private String description;
    private Configuration configuration;
}