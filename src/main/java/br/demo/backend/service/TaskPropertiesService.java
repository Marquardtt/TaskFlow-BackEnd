package br.demo.backend.service;


import br.demo.backend.model.TaskPropertiesModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TaskPropertiesService {

    TaskPropertiesService taskPropertiesService;

    public Collection<TaskPropertiesModel> findAll() {
        return taskPropertiesService.findAll();
    }

    public TaskPropertiesModel findOne(Long id) {
        return taskPropertiesService.findById(id).get();
    }

    public void save(TaskPropertiesModel taskPropertiesModel) {
        taskPropertiesService.save(taskPropertiesModel);
    }

    public void delete(Long id) {
        taskPropertiesService.deleteById(id);
    }
}
