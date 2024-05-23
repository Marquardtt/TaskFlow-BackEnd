package br.demo.backend.model.dtos.user;

import br.demo.backend.model.Configuration;
import br.demo.backend.model.Permission;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPutDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String surname;
    @Email
    private String mail;
    private String phone;
    private String description;
    private Configuration configuration;
    private boolean authenticate;
    private Collection<Permission> permissions;

}