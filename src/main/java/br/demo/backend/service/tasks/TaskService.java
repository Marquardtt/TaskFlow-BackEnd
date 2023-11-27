package br.demo.backend.service.tasks;


import br.demo.backend.model.Project;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.tasks.TaskPostDTO;
import br.demo.backend.repository.tasks.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    public Task save(TaskPostDTO taskPostDTO) {
        Task taskEmpty = taskRepository.save(new Task());

        Page page = taskPostDTO.getPage();
        Collection<Property> propertiesPage = page.getProperties();

        Project project = page.getProject();
        Collection<Property> propertiesProject = page.getProperties();


        setTaskProperties(propertiesPage, taskEmpty);
        setTaskProperties(propertiesProject, taskEmpty);

        return taskRepository.save(taskEmpty);
    }

    private void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        for (Property p : properties) {
            if (p.getType().equals(TypeOfProperty.USER)) {
                UserValue userValue = new UserValue(taskEmpty.getId(), p.getId(), new HashSet<>(), taskEmpty, p);
                taskEmpty.getUserProperties().add(userValue);
            } else if (p.getType().equals(TypeOfProperty.CHECKBOX) ||
            p.getType().equals(TypeOfProperty.TAG)) {
                Multivalued multivalued = new Multivalued(taskEmpty.getId(), p.getId(), new ArrayList<>(), taskEmpty, p);
                taskEmpty.getMultiProperties().add(multivalued);
            } else {
                Univalued univalued = new Univalued(taskEmpty.getId(), p.getId(), "", taskEmpty, p);
                taskEmpty.getUniProperties().add(univalued);
            }
        }
    }

    public Collection<Task> findByName(String name) {
        return taskRepository.findTasksByNameContains(name);
    }

    public void update(Task task) {
        taskRepository.save(task);
    }


    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
