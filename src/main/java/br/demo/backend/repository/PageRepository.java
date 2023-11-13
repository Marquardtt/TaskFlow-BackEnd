package br.demo.backend.repository;
import br.demo.backend.model.PageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageModel, Long> {
}
