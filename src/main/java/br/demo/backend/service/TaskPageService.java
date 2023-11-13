package br.demo.backend.service;


import br.demo.backend.model.TaskPageModel;
import br.demo.backend.repository.TaskPageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskPageService {

    TaskPageRepository taskPageRepository;

    public Collection<TaskPageModel> findAll() {
        return taskPageRepository.findAll();
    }

    public TaskPageModel findOne(Long id) {
        return taskPageRepository.findById(id).get();
    }

    public void save(TaskPageModel taskPageModel) {
        taskPageRepository.save(taskPageModel);
    }

    public void delete(Long id) {
        taskPageRepository.deleteById(id);
    }
}
