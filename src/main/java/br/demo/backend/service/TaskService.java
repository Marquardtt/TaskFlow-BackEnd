package br.demo.backend.service;


import br.demo.backend.model.TaskModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskService {

    TaskRepository taskRepository;

    public Collection<TaskModel> findAll() {
        return taskRepository.findAll();
    }

    public TaskModel findOne(Long id) {
        return taskRepository.findById(id).get();
    }

    public void save(TaskModel taskModel) {
        taskRepository.save(taskModel);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
