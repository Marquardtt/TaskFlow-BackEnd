package br.demo.backend.repository.relations;

import br.demo.backend.model.relations.TaskPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPageRepository extends JpaRepository<TaskPage, Long> {
}
