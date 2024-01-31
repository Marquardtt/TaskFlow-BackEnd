package br.demo.backend.service.tasks;


import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.CanvasRepository;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.repository.relations.TaskValueRepository;
import br.demo.backend.service.ResolveStackOverflow;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskValueRepository taskValueRepository;
    private PageRepository pageRepositorry;
    private CommonPageRepository commonPageRepository;
    private ProjectRepository projectRepository;
    private CanvasRepository canvasRepository;


    public Collection<Task> findAll() {
        Collection<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            ResolveStackOverflow.resolveStackOverflow(task);
        }
        return tasks;
    }

    public Task findOne(Long id) {
        Task task = taskRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(task);
        return task;
    }

    public Task save(Long idpage, Long userId) {

        Page page = pageRepositorry.findById(idpage).get();
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
        Task task = taskRepository.save(taskEmpty);
        addTaskToPage(task, page.getId());
        ResolveStackOverflow.resolveStackOverflow(task);
        return task;
    }

    private void addTaskToPage(Task task, Long pageId) {
        Page page = pageRepositorry.findById(pageId).get();
        if (page instanceof CommonPage) {
            CommonPage commonPage = (CommonPage) page;
            commonPage.getTasks().add(new TaskPage(null, task, 0.0, 0.0, 0));
            commonPageRepository.save(commonPage);
        }else{
            Canvas canvas = (Canvas) page;
            canvas.getTasks().add(new TaskPage(null, task, 0.0, 0.0, 0));
            canvasRepository.save(canvas);
        }
    }

    public void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        for (Property p : properties) {
           setTaskProperty(p, taskEmpty);
        }
    }

    public void setTaskProperty(Property p, Task taskEmpty){
        Value value = null;
        if (p.getType() == TypeOfProperty.SELECT) {
            value = new UniOptionValued();
        } else if (p.getType() == TypeOfProperty.RADIO || p.getType() == TypeOfProperty.CHECKBOX || p.getType() == TypeOfProperty.TAG) {
            value = new MultiOptionValued();
        } else if (p.getType() == TypeOfProperty.TEXT) {
            value = new TextValued();
        } else if (p.getType() == TypeOfProperty.DATE) {
            value = new DateValued();
        } else if (p.getType() == TypeOfProperty.NUMBER || p.getType() == TypeOfProperty.PROGRESS) {
            value = new NumberValued();
        } else if (p.getType() == TypeOfProperty.TIME) {
            value = new TimeValued();
        } else if (p.getType() == TypeOfProperty.ARCHIVE) {
            value = new ArchiveValued();
        } else if (p.getType() == TypeOfProperty.USER) {
            value = new UserValued();
        }
        TaskValue taskValue = new TaskValue(null, p, value);
        taskEmpty.getProperties().add(taskValue);
    }

    public Collection<Task> findByName(String name) {
        Collection<Task> tasks = taskRepository.findTasksByNameContains(name);
        for (Task task : tasks) {
            ResolveStackOverflow.resolveStackOverflow(task);
        }
        return tasks;
    }

    public void update(Task task) {
        taskRepository.save(task);
    }

    public void delete(Long id, Long userId) {
        User user = userRepository.findById(userId).get();
        Task task = taskRepository.findById(id).get();
        task.setDeleted(true);
        task.getLogs().add(new Log(null, "Task deleted", Action.DELETE, user, LocalDateTime.now()));
    }

    public void redo(Long id, Long userId) {
        Task task = taskRepository.findById(id).get();
        task.setDeleted(false);
        task.getLogs().add(new Log(null, "Task Redo", Action.REDO, new User(userId), LocalDateTime.now()));
    }

    public Collection<Task> getTasksToday(Long id) {
        User user = userRepository.findById(id).get();
        TaskValue value = taskValueRepository.findTaskValuesByProperty_TypeAndValueContaining(TypeOfProperty.USER, user);
        Collection<Task> tasks = taskRepository.findTasksByPropertiesContaining(value);
        Collection<Task> tasksToday = new ArrayList<>();
        for (Task t : tasks) {
            ResolveStackOverflow.resolveStackOverflow(t);
            for (TaskValue tp : t.getProperties()) {
                Property p = tp.getProperty();
                if (p.getType() == TypeOfProperty.DATE && ((Date) p).getScheduling()) {
                    if (tp.getValue().getValue().equals(LocalDate.now())) {
                        tasksToday.add(t);
                    }
                }
            }
        }
        return tasksToday;
    }

    public Collection<Task> getTasksOfMonth(Integer month, Long pageId, Long propertyId) {
        CommonPage page = commonPageRepository.findById(pageId).get();
        Collection<Task> tasks = new ArrayList<>();
        for (TaskPage task : page.getTasks()) {
            ResolveStackOverflow.resolveStackOverflow(task.getTask());
            for (TaskValue taskValue : task.getTask().getProperties()) {
                if (taskValue.getProperty().getId().equals(propertyId)) {
                    if (((LocalDateTime) taskValue.getValue()
                            .getValue()).getMonthValue() == month) {
                        tasks.add(task.getTask());
                    }
                }
            }
        }
        return tasks;
    }
}
