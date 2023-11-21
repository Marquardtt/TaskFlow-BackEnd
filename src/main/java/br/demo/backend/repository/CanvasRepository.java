package br.demo.backend.repository;

import br.demo.backend.model.pages.CanvasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanvasRepository extends JpaRepository<CanvasModel, Long> {
}
