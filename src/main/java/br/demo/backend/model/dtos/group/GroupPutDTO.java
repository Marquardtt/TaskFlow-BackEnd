package br.demo.backend.model.dtos.group;

import br.demo.backend.model.Permission;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.interfaces.IWithMembers;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupPutDTO  implements IWithMembers {
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