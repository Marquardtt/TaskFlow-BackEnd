package br.demo.backend.repository.pages;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    Collection<Page> findByTasks_Task(Task task);
    Collection<Page> findByPropertiesContaining(Property prop);
}
