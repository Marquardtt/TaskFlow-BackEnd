package br.demo.backend.repository.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

     Collection<Task> findTasksByNameContains(String name);

     Task findByPropertiesContaining(TaskValue prop);

}
