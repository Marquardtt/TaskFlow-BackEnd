package br.demo.backend.service.relations;


import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskCanvasService {

    private TaskCanvasRepository taskCanvasRepository;

    public Collection<TaskCanvas> findAll() {
        return taskCanvasRepository.findAll();
    }

    public TaskCanvas findOne(Long id) {
        return taskCanvasRepository.findById(id).get();
    }

    public void save(TaskCanvas userGroup) {
        taskCanvasRepository.save(userGroup);
    }

    public void delete(Long id) {
        taskCanvasRepository.deleteById(id);
    }
}
