package br.demo.backend.model;

import br.demo.backend.model.enums.TypePermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_permission")
public class Permission {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private TypePermission permission;

    public Permission(Long id){
        this.id = id;
    }
}
