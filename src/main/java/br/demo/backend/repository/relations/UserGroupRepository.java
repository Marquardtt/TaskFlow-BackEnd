package br.demo.backend.repository.relations;

import br.demo.backend.model.relations.PermissionProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<PermissionProject, Long> {

    public PermissionProject findPermissionByUserIdAndProjectId(Long userId, Long projectId);
}
