package br.demo.backend.service;


import br.demo.backend.model.TaskPageId;
import br.demo.backend.model.TaskPage;
import br.demo.backend.repository.TaskPageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskPageService {

    TaskPageRepository taskPageRepository;

    public Collection<TaskPage> findAll() {
        return taskPageRepository.findAll();
    }

    public TaskPage findOne(TaskPageId id) {
        return taskPageRepository.findById(id).get();
    }

    public void save(TaskPage taskPage) {
        taskPageRepository.save(taskPage);
    }

    public void delete(TaskPageId id) {
        taskPageRepository.deleteById(id);
    }
}
