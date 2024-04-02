package br.demo.backend.service.tasks;



import br.demo.backend.repository.values.UserValuedRepository;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.service.NotificationService;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.repository.relations.PropertyValueRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.utils.AutoMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private PropertyValueRepository taskValueRepository;
    private PageRepository pageRepositorry;
    private OrderedPageRepository orderedPageRepository;
    private ProjectRepository projectRepository;
    private CanvasPageRepository canvasPageRepository;
    private AutoMapper<Task> autoMapper;
    private NotificationService notificationService;
    private TaskPageRepository taskPageRepository;
    private UserValuedRepository userValuedRepository;


    public TaskGetDTO save(Long idpage, String userId) {

        Page page = pageRepositorry.findById(idpage).get();
        User user = userRepository.findByUserDetailsEntity_Username(userId).get();

        Task taskEmpty = taskRepository.save(new Task());

        Collection<Property> propertiesPage = page.getProperties();

        Project project = projectRepository.findByPagesContaining(page);
        Collection<Property> propertiesProject = project.getProperties();


        taskEmpty.setProperties(new HashSet<>());
        setTaskProperties(propertiesPage, taskEmpty);
        setTaskProperties(propertiesProject, taskEmpty);

        taskEmpty.setLogs(new HashSet<>());
        taskEmpty.getLogs().add(new Log(null, "Task created", Action.CREATE, user, LocalDateTime.now(), null) );

        Task task = taskRepository.save(taskEmpty);
        addTaskToPage(task, page.getId());
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
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
        Collection<PropertyValue> propertyValues = new ArrayList<>(properties.stream().map(this::setTaskProperty).toList());
        propertyValues.addAll(taskEmpty.getProperties());
        taskEmpty.setProperties(propertyValues);
        System.out.println(ModelToGetDTO.tranform(taskEmpty));
    }

    public PropertyValue setTaskProperty(Property p) {
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
        return new PropertyValue(null, p, value);
    }

    public TaskGetDTO update(Task taskDTO, Boolean patching) {
        Task oldTask = taskRepository.findById(taskDTO.getId()).get();
        Task task = patching ? oldTask : new Task();
        autoMapper.map(taskDTO, task, patching);
        task.setLogs(oldTask.getLogs());
        task.setCompleted(false);
        task.setDeleted(false);
        if(!task.getName().equals(oldTask.getName())){
            //TODO: Mudar o user null para o user logado
            task.getLogs().add(new Log(null, "The task's name was changed to '"+
                    task.getName()+"'", Action.UPDATE, new User("jonatas"),
                    LocalDateTime.now(), null));
        }
        createUpdateLogs(task, oldTask);
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        Collection<Message> comments = task.getComments().stream().filter(c -> !oldTask.getComments().contains(c)).toList();
        comments.forEach(c -> notificationService.generateNotification(TypeOfNotification.COMMENTS, task.getId(), c.getId()));
        return ModelToGetDTO.tranform(taskRepository.save(task));
    }

    private void createUpdateLogs(Task task, Task old){
        Collection<Log> logs = task.getProperties().stream()
                .map(prop -> {
                    PropertyValue first = old.getProperties().stream()
                            .filter(p-> p.getValue().getId().equals(prop.getValue().getId()))
                            .findFirst()
                            .orElse(null);

                    if (first != null && !prop.getValue().getValue().equals(first.getValue().getValue())) {
                        //TODO: Mudar o user null para o user logado
                        PropertyValue propertyValue = new PropertyValue(first);
                        return new Log(null, descriptionUpdate(prop), Action.UPDATE,
                                new User("jonatas"), LocalDateTime.now(), propertyValue);
                    }
                    return null; // Se não há alteração, retorna null
                })
                .filter(Objects::nonNull).toList();
        task.getLogs().addAll(logs);
    }

    private String descriptionUpdate(PropertyValue value){
        String base = "The property '"+value.getProperty().getName()+"' was changed to '";
        base += switch (value.getProperty().getType()){
            case DATE -> formatDate(value)+"'";
            case SELECT, RADIO -> ((Option)value.getValue().getValue()).getName()+"'";
            case TIME -> (formateDuration(((Intervals)value.getValue().getValue()).getTime()))+"'";
            case CHECKBOX, TAG -> listString(((Collection<Option>)value.getValue().getValue())
                    .stream().map(Option::getName).toList())+"'";
            case USER -> listString(((Collection<User>)value.getValue().getValue())
                    .stream().map(u -> u.getUserDetailsEntity().getUsername()).toList())+"'";
            case ARCHIVE -> ((Archive)value.getValue().getValue()).getName() +"."+
                    ((Archive)value.getValue().getValue()).getType()+"'";
            case TEXT, NUMBER -> value.getValue().getValue()+"'";
            case PROGRESS -> value.getValue().getValue()+"%'";
        };
        return base;
    }


    private String listString(Collection<String> strs){
        StringJoiner base = new StringJoiner(", ", "", " and ");
        for(String str : strs){
            base.add(str);
        }
        return base.toString();
    }

    private String formateDuration (Duration value){
        long hours = value.toHours();
        long minutes = value.minusHours(hours).toMinutes();
        long seconds = value.minusHours(hours).minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String formatDate (PropertyValue value){
        DateTimeFormatter formatter = null;
        if(((Date)value.getProperty()).getIncludesHours()){
            formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(Locale.getDefault());
        }else{
            formatter =  DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(Locale.getDefault());
        }
        return ((LocalDateTime)value.getValue().getValue()).format(formatter);
    }



    public void delete(Long id) {
        String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(userId).get();
        Task task = taskRepository.findById(id).get();
        task.setDateDeleted(LocalDateTime.now());
        task.setDeleted(true);
        task.getLogs().add(new Log(null, "Task deleted", Action.DELETE, user, LocalDateTime.now(), null));
        taskRepository.save(task);
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
    }

    public void deletePermanent(Long id) {
        taskPageRepository.deleteAll(taskPageRepository.findAllByTask_Id(id));
        taskRepository.deleteById(id);
    }

    public TaskGetDTO redo(Long id, String userId) {
        Task task = taskRepository.findById(id).get();
        task.setDeleted(false);
        task.getLogs().add(new Log(null, "Task Redo", Action.REDO, new User(userId), LocalDateTime.now(), null));
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        return ModelToGetDTO.tranform(taskRepository.save(task));
    }

    public Collection<TaskGetDTO> getDeletedTasks(Long projectId){
        Project project = projectRepository.findById(projectId).get();
        Collection<Page> pages = project.getPages();
        return pages.stream().map(Page::getTasks).flatMap(Collection::stream)
                .filter(t -> t.getTask().getDeleted()).map(TaskPage::getTask).map(ModelToGetDTO::tranform)
                .distinct().toList();
    }

    public Collection<TaskGetDTO> getTasksToday(String id) {
        User user = userRepository.findByUserDetailsEntity_Username(id).get();
        Collection<Value> values = userValuedRepository.findAllByUsersContaining(user);
        Collection<PropertyValue> taskValues =
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

    public TaskGetDTO complete(Long id) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        Task task = taskRepository.findById(id).get();
        task.setCompleted(true);
        task.setDateCompleted(LocalDateTime.now());
        task.getLogs().add(new Log(null, "Task completed", Action.COMPLETE, user, LocalDateTime.now(), null));
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        return ModelToGetDTO.tranform(taskRepository.save(task));
    }
}
