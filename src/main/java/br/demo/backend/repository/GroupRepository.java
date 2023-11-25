package br.demo.backend.repository;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    public Group findGroupByUsers(Collection<User> users);
}
