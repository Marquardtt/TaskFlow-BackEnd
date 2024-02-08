package br.demo.backend.model;

import br.demo.backend.model.enums.TypePermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_permission")
public class Permission {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypePermission permission;
    @ManyToOne
    private Project project;

    public Permission(Long id) {
        this.id = id;
    }
}
