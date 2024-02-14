package br.demo.backend.repository.relations;
import br.demo.backend.model.ids.TaskPageId;
import br.demo.backend.model.relations.TaskOrdered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOrderedRepository extends JpaRepository<TaskOrdered, Long>{
}
