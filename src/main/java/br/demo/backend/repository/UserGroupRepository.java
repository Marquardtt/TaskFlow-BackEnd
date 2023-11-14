package br.demo.backend.repository;

import br.demo.backend.model.UserGroupId;
import br.demo.backend.model.UserGroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupModel, UserGroupId> {
}
