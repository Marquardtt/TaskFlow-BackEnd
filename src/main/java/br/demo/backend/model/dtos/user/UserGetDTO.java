package br.demo.backend.model.dtos.user;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Configuration;
import br.demo.backend.model.Notification;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserGetDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Archive picture;
    private String mail;
    private String phone;
    private String description;
    private Long points;
    private Configuration configuration;
    private boolean authenticate;
    private Collection<PermissionGetDTO> permissions;
    private Collection<Notification> notifications;


}