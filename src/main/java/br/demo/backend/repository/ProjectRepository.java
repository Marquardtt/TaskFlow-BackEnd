package br.demo.backend.repository;

import br.demo.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}