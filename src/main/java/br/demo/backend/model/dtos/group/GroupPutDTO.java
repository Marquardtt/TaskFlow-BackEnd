package br.demo.backend.model.dtos.group;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.interfaces.WithMembers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupPutDTO  implements WithMembers {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private Collection<Permission> permissions;
    private Collection<UserGetDTO> users;

    @Override
    public Collection<UserGetDTO> getMembersDTO() {
        return users;
    }
}