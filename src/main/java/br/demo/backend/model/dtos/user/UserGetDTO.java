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
    private String username;
    private String name;
    private String surname;
    private String address;
    private Archive picture;
    private String mail;
    private String phone;
    private String description;
    private Long points;
    private Configuration configuration;
    private Collection<PermissionGetDTO> permissions;
    private Collection<Notification> notifications;

    public void setAddress(String address) {
        //TODO: that decode the address
        this.address = address;
    }
}