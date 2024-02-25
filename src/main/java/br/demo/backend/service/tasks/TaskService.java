package br.demo.backend.service.tasks;


import br.demo.backend.globalfunctions.ModelToGetDTO;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.repository.relations.TaskValueRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import lombok.AllArgsConstructor;
import org.apache.el.stream.Stream;
import org.springframework.beans.BeanUtils;
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
    private OrderedPageRepository orderedPageRepository;
    private ProjectRepository projectRepository;
    private CanvasPageRepository canvasPageRepository;
    private AutoMapper<Task> autoMapper;


    public Collection<TaskGetDTO> findAll() {
        return taskRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public TaskGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(taskRepository.findById(id).get());
    }

    public TaskGetDTO save(Long idpage, String userId) {

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

        Task task = taskRepository.save(taskEmpty);
        addTaskToPage(task, page.getId());

        return ModelToGetDTO.tranform(task);
    }

    public void addTaskToPage(Task task, Long pageId) {
        Page page = pageRepositorry.findById(pageId).get();
        if(page.getType().equals(TypeOfPage.CANVAS)) {
            page.getTasks().add(new TaskCanvas(null, task, 0.0, 0.0));
            canvasPageRepository.save((CanvasPage) page);
        } else{
            page.getTasks().add(new TaskOrdered(null, task, 0));
             if(page instanceof OrderedPage){
                 orderedPageRepository.save((OrderedPage) page);
             }else{
                 pageRepositorry.save(page);
             }
        }
    }


    public void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        Collection<TaskValue> taskValues = new ArrayList<>(properties.stream().map(this::setTaskProperty).toList());
        taskValues.addAll(taskEmpty.getProperties());
        taskEmpty.setProperties(taskValues);
        System.out.println(ModelToGetDTO.tranform(taskEmpty));
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

    public Collection<TaskGetDTO> findByName(String name) {
        return taskRepository.findTasksByNameContains(name).stream().map(ModelToGetDTO::tranform).toList();
    }

    public void update(Task taskDTO, Boolean patching) {
        Task task = patching ? taskRepository.findById(taskDTO.getId()).get() : new Task();
        autoMapper.map(taskDTO, task, patching);
        taskRepository.save(task);
    }

    public void delete(Long id, String userId) {
        User user = userRepository.findById(userId).get();
        Task task = taskRepository.findById(id).get();
        task.setDeleted(true);
        task.getLogs().add(new Log(null, "Task deleted", Action.DELETE, user, LocalDateTime.now()));
    }

    public void redo(Long id, String userId) {
        Task task = taskRepository.findById(id).get();
        task.setDeleted(false);
        task.getLogs().add(new Log(null, "Task Redo", Action.REDO, new User(userId), LocalDateTime.now()));
    }

    public Collection<TaskGetDTO> getTasksToday(String id) {
        User user = userRepository.findById(id).get();
        TaskValue value = taskValueRepository.findTaskValuesByProperty_TypeAndValueContaining(TypeOfProperty.USER, user);
        Collection<Task> tasks = taskRepository.findTasksByPropertiesContaining(value);

        return tasks.stream().filter(t ->
                        t.getProperties().stream().anyMatch(p ->
                                p.getProperty().getType().equals(TypeOfProperty.DATE) &&
                                        ((Date) p.getProperty()).getScheduling() &&
                                        p.getValue().getValue().equals(LocalDate.now())))
                .map(ModelToGetDTO::tranform).toList();
    }

    public Collection<TaskPageGetDTO> getTasksOfMonth(Integer month, Long pageId, Long propertyId) {
        OrderedPage page = orderedPageRepository.findById(pageId).get();

        return page.getTasks().stream().filter(t ->
                        t.getTask().getProperties().stream().anyMatch(p ->
                                p.getProperty().getId().equals(propertyId) &&
                                        ((LocalDateTime) p.getValue()
                                                .getValue()).getMonthValue() == month))
                .map(ModelToGetDTO::tranform).toList();
    }

}
