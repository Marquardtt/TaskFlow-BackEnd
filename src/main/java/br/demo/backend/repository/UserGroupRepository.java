package br.demo.backend.repository;

import br.demo.backend.model.UserGroupId;
import br.demo.backend.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
}
