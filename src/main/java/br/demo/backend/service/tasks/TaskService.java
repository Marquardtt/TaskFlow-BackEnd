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
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
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
    private AutoMapper<Task> autoMapper;



    public Collection<Task> findAll() {
        Collection<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public Task findOne(Long id) {
        Task task = taskRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(task);
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
        return ResolveStackOverflow.resolveStackOverflow(task);
    }

    private void addTaskToPage(Task task, Long pageId) {
        Page page = pageRepositorry.findById(pageId).get();
        if (page instanceof CommonPage) {
            CommonPage commonPage = (CommonPage) page;
            commonPage.getTasks().add(new TaskPage(null, task, 0.0, 0.0, 0));
            commonPageRepository.save(commonPage);
        } else {
            Canvas canvas = (Canvas) page;
            canvas.getTasks().add(new TaskPage(null, task, 0.0, 0.0, 0));
            canvasRepository.save(canvas);
        }
    }

    public void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        taskEmpty.getProperties().addAll(properties.stream().map(p -> setTaskProperty(p)).toList());
    }

    public TaskValue setTaskProperty(Property p) {
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
        return new TaskValue(null, p, value);
    }

    public Collection<Task> findByName(String name) {
        Collection<Task> tasks = taskRepository.findTasksByNameContains(name);
        return tasks.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public void update(Task taskDTO, Boolean patching) {
        Task task = patching ? taskRepository.findById(taskDTO.getId()).get() : new Task();
        autoMapper.map(taskDTO, task, patching);
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

        return tasks.stream().filter(t ->
                t.getProperties().stream().anyMatch(p ->
                                p.getProperty().getType().equals(TypeOfProperty.DATE) &&
                                        ((Date) p.getProperty()).getScheduling() &&
                                        p.getValue().getValue().equals(LocalDate.now())))
                .map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public Collection<Task> getTasksOfMonth(Integer month, Long pageId, Long propertyId) {
        CommonPage page = commonPageRepository.findById(pageId).get();

        return page.getTasks().stream().filter(t ->
                        t.getTask().getProperties().stream().anyMatch(p ->
                                p.getProperty().getId().equals(propertyId) &&
                                        ((LocalDateTime) p.getValue()
                                                .getValue()).getMonthValue() == month))
                .map(t -> ResolveStackOverflow.resolveStackOverflow(t.getTask())).toList();

    }

}
