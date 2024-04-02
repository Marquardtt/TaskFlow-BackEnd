package br.demo.backend.repository;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {


    Collection<Permission> findByProject(Project project);
}
