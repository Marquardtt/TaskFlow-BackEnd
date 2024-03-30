package br.demo.backend.repository;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Collection<Group> findGroupsByOwnerOrUsersContaining(User user, User user2);

    Collection<Group> findGroupsByPermissions_Project(Project project);

    Group findGroupByPermissions_ProjectAndUsersContaining(Project project, User user);


}
