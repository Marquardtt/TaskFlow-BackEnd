package br.demo.backend.repository;

import br.demo.backend.model.TaskPropertiesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPropertiesRepository extends JpaRepository<TaskPropertiesModel, Long> {
}
