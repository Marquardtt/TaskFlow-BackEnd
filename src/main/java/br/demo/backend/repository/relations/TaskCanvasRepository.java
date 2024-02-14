package br.demo.backend.repository.relations;
import br.demo.backend.model.ids.TaskPageId;
import br.demo.backend.model.relations.TaskCanvas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCanvasRepository extends JpaRepository<TaskCanvas, Long>{
}
