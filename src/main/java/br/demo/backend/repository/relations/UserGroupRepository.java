package br.demo.backend.repository.relations;

import br.demo.backend.model.relations.ids.UserGroupId;
import br.demo.backend.model.relations.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
}
