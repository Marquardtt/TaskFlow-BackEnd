package br.demo.backend.repository.relations;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCanvasRepository extends JpaRepository<TaskCanvas, Long>{
}
