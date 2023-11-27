package br.demo.backend.service.tasks;


import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.tasks.TaskPostDTO;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;

    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(Long id) {
        return taskRepository.findById(id).get();
    }

    public Task save(Page page, User user) {
        Task taskEmpty = taskRepository.save(new Task());

        Collection<Property> propertiesPage = page.getProperties();

        Project project = page.getProject();
        Collection<Property> propertiesProject = project.getProperties();


        setTaskProperties(propertiesPage, taskEmpty);
        setTaskProperties(propertiesProject, taskEmpty);

        taskEmpty.getLogs().add(new Log(null, "Task created", Action.CREATE, user, LocalDateTime.now()));

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

    public void delete(Long id, User user) {

        Task task = taskRepository.findById(id).get();
        task.setDeleted(true);
        task.getLogs().add(new Log(null, "Task deleted", Action.DELETE, user, LocalDateTime.now()));
    }
    public void redo(Long id, User user) {
        Task task = taskRepository.findById(id).get();
        task.setDeleted(false);
        task.getLogs().add(new Log(null, "Task Redo", Action.REDO, user, LocalDateTime.now()));
    }

    public Collection<Task> getTasksToday(Long id) {
        User user = userRepository.findById(id).get();
        Collection<Task> tasks = taskRepository.findTasksByUserPropertiesContaining(user);
        Collection<Task> tasksToday = new ArrayList<>();
        for(Task t : tasks){
            for(Univalued u : t.getUniProperties()){
                Property p = u.getProperty();
                if(p.getType() == TypeOfProperty.DATE &&((Date)p).getScheduling()){
                    if(LocalDate.parse(u.getValue()).equals(LocalDate.now())){
                        tasksToday.add(t);
                    }
                }
            }
        }
        return tasksToday;
    }
}
