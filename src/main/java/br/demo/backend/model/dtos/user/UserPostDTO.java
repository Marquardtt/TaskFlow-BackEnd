package br.demo.backend.model.dtos.user;

import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPostDTO {

    private String name;
    private String surname;
    private String mail;
    private boolean authenticate;
    private UserDatailEntity userDetailsEntity;
}