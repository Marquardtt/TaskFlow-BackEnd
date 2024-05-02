package br.demo.backend.model;

import br.demo.backend.model.enums.TypePermission;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @Column
    private String name;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePermission permission = TypePermission.READ;
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Project project;
    private Boolean isDefault = false;

    public Permission(Long id) {
        this.id = id;
    }
}
