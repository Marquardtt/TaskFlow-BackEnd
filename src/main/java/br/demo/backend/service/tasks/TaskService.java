package br.demo.backend.service.tasks;


import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.repository.tasks.TaskValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;
    TaskValueRepository taskValueRepository;
    PageRepository pageRepositorry;
    ProjectRepository projectRepository;

    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(Long id) {
        return taskRepository.findById(id).get();
    }

    public Task save(Page pagePost, Long userId) {
        Page page = pageRepositorry.findById(pagePost.getId()).get();
        User user = new User(userId);
        Task taskEmpty = taskRepository.save(new Task());
        Collection<Property> propertiesPage = page.getProperties();
        Project project = projectRepository.findByPagesContaining(page);
        Collection<Property> propertiesProject = project.getProperties();

        taskEmpty.setProperties(new HashSet<>());
        setTaskProperties(propertiesPage, taskEmpty);
        setTaskProperties(propertiesProject, taskEmpty);

        taskEmpty.setLogs(new HashSet<>());
        taskEmpty.getLogs().add(new Log(null, "Task created", Action.CREATE, user, LocalDateTime.now()));

        System.out.println(taskEmpty);
        return taskRepository.save(taskEmpty);
    }

    private void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        for (Property p : properties) {
            Value value = null;
            if(p.getType() == TypeOfProperty.SELECT){
//                value = new UniOptionValued((new ArrayList<>(((Select) p).getOptions())).get(0));
                value = new UniOptionValued();
            }else if(p.getType() == TypeOfProperty.RADIO || p.getType() == TypeOfProperty.CHECKBOX || p.getType() == TypeOfProperty.TAG){
                value = new MultiOptionValued();
            }else if(p.getType() == TypeOfProperty.TEXT){
                value = new TextValued();
            } else if (p.getType() == TypeOfProperty.DATE) {
                value = new DateValued();
            } else if (p.getType() == TypeOfProperty.NUMBER || p.getType() == TypeOfProperty.PROGRESS) {
                value = new NumberValued();
            } else if (p.getType() == TypeOfProperty.TIME) {
                value = new TimeValued();
            } else if (p.getType() == TypeOfProperty.ARCHIVE) {
                value = new ArchiveValued();
            }
            else if (p.getType() == TypeOfProperty.USER) {
                value = new UserValued();
            }
            TaskValue taskValue = new TaskValue(null, p, value);
            taskEmpty.getProperties().add(taskValue);
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
        TaskValue value = taskValueRepository.findTaskValuesByProperty_TypeAndValueContaining(TypeOfProperty.USER, user);
        Collection<Task> tasks = taskRepository.findTasksByPropertiesContaining(value);
        Collection<Task> tasksToday = new ArrayList<>();
        for(Task t : tasks){
            for(TaskValue tp : t.getProperties()){
                Property p = tp.getProperty();
                if(p.getType() == TypeOfProperty.DATE &&((Date)p).getScheduling()){
                    if(tp.getValue().getValue().equals(LocalDate.now())){
                        tasksToday.add(t);
                    }
                }
            }
        }
        return tasksToday;
    }
}
