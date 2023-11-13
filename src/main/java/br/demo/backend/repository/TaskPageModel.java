package br.demo.backend.repository;

import br.demo.backend.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPageModel extends JpaRepository<TaskModel, Long>{
}
