package br.demo.backend.model;

import br.demo.backend.model.enums.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "db_permission")
public class PermissionModel {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Collection<Permission> permissions;
}
