package br.demo.backend.service;


import br.demo.backend.model.Task;
import br.demo.backend.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskService {

    TaskRepository taskRepository;

    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(Long id) {
        return taskRepository.findById(id).get();
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
