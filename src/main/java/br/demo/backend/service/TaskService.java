package br.demo.backend.service;


import br.demo.backend.model.Task;
import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        Task taskEmpty = taskRepository.save(new Task());
        Long id = taskEmpty.getId();
        BeanUtils.copyProperties(task, taskEmpty);
        taskEmpty.setId(id);
        try {
            for (Multivalued m : taskEmpty.getMultiProperties()) {
                m.setTaskId(id);
            }
        } catch (NullPointerException ignore) {
        }
        try {
            for (Univalued u : taskEmpty.getUniProperties()) {
                u.setTaskId(id);
            }
        } catch (NullPointerException ignore) {
        }
        try {
            for (UserValue u : taskEmpty.getUserProperties()) {
                u.setTaskId(id);
            }
        } catch (NullPointerException ignore) {
        }
        this.update(taskEmpty);
    }

    public void update(Task task) {
        taskRepository.save(task);
    }


    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
