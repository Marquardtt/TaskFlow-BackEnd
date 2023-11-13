package br.demo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPropertiesModel extends JpaRepository<TaskPropertiesModel, Long> {
}
