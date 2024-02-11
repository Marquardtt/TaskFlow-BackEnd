package br.demo.backend.repository.pages;

import br.demo.backend.model.pages.CanvasPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanvasPageRepository extends JpaRepository<CanvasPage, Long> {
}
