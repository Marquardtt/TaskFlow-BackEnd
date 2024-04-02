package br.demo.backend.repository.tasks;

import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

     Collection<Task> findTasksByNameContains(String name);

     Task findByPropertiesContaining(TaskValue prop);
    public Collection<Task> findTasksByPropertiesContaining(PropertyValue prop);

}
