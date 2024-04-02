package br.demo.backend.service.tasks;


import br.demo.backend.repository.values.UserValuedRepository;
import br.demo.backend.repository.values.ValueRepository;
import br.demo.backend.utils.ModelToGetDTO;
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
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;

import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;

import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.repository.relations.TaskValueRepository;
import br.demo.backend.utils.AutoMapper;
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
    private OrderedPageRepository orderedPageRepository;
    private ProjectRepository projectRepository;
    private CanvasPageRepository canvasPageRepository;
    private AutoMapper<Task> autoMapper;
    private TaskPageRepository taskPageRepository;
    private UserValuedRepository userValuedRepository;



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
        System.out.println(ModelToGetDTO.tranform(page));
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
        if (p.getType() == TypeOfProperty.RADIO ||p.getType() == TypeOfProperty.SELECT) {
            value = new UniOptionValued();
        } else if ( p.getType() == TypeOfProperty.CHECKBOX || p.getType() == TypeOfProperty.TAG) {
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
        taskRepository.save(task);
    }

    public void deletePermanent(Long id) {
        taskPageRepository.deleteAll(taskPageRepository.findAllByTask_Id(id));
        taskRepository.deleteById(id);
    }

    public void redo(Long id, String userId) {
        Task task = taskRepository.findById(id).get();
        task.setDeleted(false);
        task.getLogs().add(new Log(null, "Task Redo", Action.REDO, new User(userId), LocalDateTime.now()));
        taskRepository.save(task);
    }

    public Collection<TaskGetDTO> getDeletedTasks(Long projectId){
        Project project = projectRepository.findById(projectId).get();
        Collection<Page> pages = project.getPages();
        return pages.stream().map(Page::getTasks).flatMap(Collection::stream)
                .filter(t -> t.getTask().getDeleted()).map(TaskPage::getTask).map(ModelToGetDTO::tranform)
                .distinct().toList();
    }

    public Collection<TaskGetDTO> getTasksToday(String id) {
        User user = userRepository.findById(id).get();
        Collection<Value> values = userValuedRepository.findAllByUsersContaining(user);
        Collection<TaskValue> taskValues =
                values.stream().map(v -> taskValueRepository
                                .findByProperty_TypeAndValue(TypeOfProperty.USER, v))
                        .toList();
        Collection<Task> tasks = taskValues.stream().map(tVl -> taskRepository.findByPropertiesContaining(tVl)).toList();

        return tasks.stream().filter(t ->
                        t.getProperties().stream().anyMatch(p ->
                                p.getProperty().getType().equals(TypeOfProperty.DATE)
                                        &&
                                        ((((Date) p.getProperty()).getScheduling()
                                                &&
                                                !user.getConfiguration().getInitialPageTasksPerDeadline())
                                        ||
                                        (((Date) p.getProperty()).getDeadline()
                                                &&
                                                user.getConfiguration().getInitialPageTasksPerDeadline()))
                                        &&
                                        p.getValue().getValue() != null
                                        &&
                                        compareToThisDay((LocalDateTime) p.getValue().getValue())))
                .map(ModelToGetDTO::tranform).toList();
    }

    private Boolean compareToThisDay(LocalDateTime time){
        return time.getMonthValue() == LocalDate.now().getMonthValue() &&
                time.getYear() == time.getYear() &&
                time.getDayOfMonth() == time.getDayOfMonth();
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
