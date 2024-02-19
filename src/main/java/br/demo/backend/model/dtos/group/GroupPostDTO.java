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
public class GroupPostDTO {
    private String name;
    private String description;
    private Collection<Permission> permissions;
    private User owner;
    private Collection<User> users;

}