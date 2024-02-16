package br.demo.backend.model.dtos.group;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupPutDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private Collection<Permission> permissions;
    private Collection<User> users;
}