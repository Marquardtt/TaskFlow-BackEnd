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
    private String username;
    private String name;
    private String surname;
    private String address;
    @Email
    private String mail;
    private String phone;
    private String description;
    private Configuration configuration;
    private Collection<Permission> permissions;
}