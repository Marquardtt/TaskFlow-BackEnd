package br.demo.backend.repository;

import br.demo.backend.model.TaskModel;
import br.demo.backend.model.TaskPageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPageRepository extends JpaRepository<TaskPageModel, Long>{
}
