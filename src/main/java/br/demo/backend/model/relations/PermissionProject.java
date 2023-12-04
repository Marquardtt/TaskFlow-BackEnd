package br.demo.backend.model.relations;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_permission_project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Project project;
    @ManyToOne
    private Permission permission;

}
