package br.demo.backend.service;


import br.demo.backend.model.TaskPropertiesModel;
import br.demo.backend.repository.TaskPropertiesRepository;
import br.demo.backend.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskPropertiesService {

    TaskPropertiesRepository taskPropertiesRepository;

    public Collection<TaskPropertiesModel> findAll() {
        return taskPropertiesRepository.findAll();
    }

    public TaskPropertiesModel findOne(Long id) {
        return taskPropertiesRepository.findById(id).get();
    }

    public void save(TaskPropertiesModel taskPropertiesModel) {
        taskPropertiesRepository.save(taskPropertiesModel);
    }

    public void delete(Long id) {
        taskPropertiesRepository.deleteById(id);
    }
}
