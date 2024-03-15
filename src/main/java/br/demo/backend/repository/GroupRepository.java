package br.demo.backend.repository;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Collection<Group> findGroupsByUsersContaining(User user);

    Collection<Group> findGroupsByPermissions_Project(Project project);


}
