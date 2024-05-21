package br.demo.backend.service.tasks;


import br.demo.backend.exception.TaskAlreadyCompleteException;
import br.demo.backend.exception.TaskAlreadyDeletedException;
import br.demo.backend.google.service.GoogleCalendarService;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.values.DateValued;
import br.demo.backend.model.values.DateWithGoogle;
import br.demo.backend.repository.DateWithGoogleRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.LogService;
import br.demo.backend.service.PropertyValueService;
import br.demo.backend.service.UserService;
import br.demo.backend.utils.IdProjectValidation;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.service.NotificationService;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.utils.AutoMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private PageRepository pageRepositorry;
    private OrderedPageRepository orderedPageRepository;
    private ProjectRepository projectRepository;
    private CanvasPageRepository canvasPageRepository;
    private AutoMapper<Task> autoMapper;
    private NotificationService notificationService;
    private TaskPageRepository taskPageRepository;
    private LogService logService;
    private UserService userService;
    private PropertyValueService propertyValueService;
    private IdProjectValidation validation;
    private UserRepository userRepository;
    private DateWithGoogleRepository dateWithGoogleRepository;

    private GoogleCalendarService googleCalendarService;

    public TaskGetDTO save(Long idpage, Long idProject) {
        Page page = pageRepositorry.findById(idpage).get();
        Task taskEmpty = taskRepository.save(new Task());
        validation.ofObject(idProject, page.getProject());

        //add the properties at the task
        addPropertiesAtANewTask(page, taskEmpty);
        //generate the log of create a task
        logService.generateLog(Action.CREATE, taskEmpty);

        Task task = taskRepository.save(taskEmpty);
        //add task to the page setting its type (canvas or ordered)
        addTaskToPage(task, page);
        TaskGetDTO taskGetDTO = ModelToGetDTO.tranform(task);
        //generate the notifications
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        return taskGetDTO;
    }

    private void addPropertiesAtANewTask(Page page, Task taskEmpty) {
        //get the task's page's and task's project's properties
        if (taskEmpty.getProperties() == null) {
            taskEmpty.setProperties(new HashSet<>());
        }
        Collection<Property> propertiesPage = page.getProperties().stream().filter(p -> taskEmpty.getProperties().stream().noneMatch(p2 -> p2.getProperty().equals(p))).toList();
        Project project = projectRepository.findByPagesContaining(page);
        Collection<Property> propertiesProject = project.getProperties().stream().filter(p -> taskEmpty.getProperties().stream().noneMatch(p2 -> p2.getProperty().equals(p))).toList();

        //set the property values at the task
        propertyValueService.setTaskProperties(propertiesPage, taskEmpty);
        propertyValueService.setTaskProperties(propertiesProject, taskEmpty);
    }

    public void addTaskToPage(Task task, Page page) {
        //this if separate tasks ata tasks at canvas or tasks at other pages
        addPropertiesAtANewTask(page, task);

        if (page.getType().equals(TypeOfPage.CANVAS)) {
            page.getTasks().add(new TaskCanvas(null, task, 0.0, 0.0));
            canvasPageRepository.save((CanvasPage) page);
        } else {
            page.getTasks().add(new TaskOrdered(null, task, 0));
            if (page instanceof OrderedPage) {
                orderedPageRepository.save((OrderedPage) page);
            } else {
                pageRepositorry.save(page);
            }
        }
    }

    public TaskGetDTO comment(Task taskDTO, Long projectId) {
        Task oldTask = taskRepository.findById(taskDTO.getId()).get();
        if (oldTask.getDeleted()) throw new TaskAlreadyDeletedException();
        Page page = pageRepositorry.findByTasks_Task(oldTask).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());
        Collection<Message> oldComments = List.copyOf(oldTask.getComments());
        oldTask.setComments(taskDTO.getComments());
        TaskGetDTO taskGetDTO = ModelToGetDTO.tranform(taskRepository.save(oldTask));
        Collection<Message> comments = oldTask.getComments().stream().filter(c ->
                oldComments.stream().noneMatch(c2 -> c2.getId().equals(c.getId()))).toList();
        //generate the notifications
        comments.forEach(c -> notificationService.generateNotification(TypeOfNotification.COMMENTS, oldTask.getId(), c.getId()));
        return taskGetDTO;
    }

    public TaskGetDTO update(Task taskDTO, Boolean patching,
                             Long projectId, Boolean isUserRequest) {
        Task oldTask = taskRepository.findById(taskDTO.getId()).get();

        if(isUserRequest){
            if (oldTask.getDeleted()) throw new TaskAlreadyDeletedException();
            if (oldTask.getCompleted()) throw new TaskAlreadyCompleteException();
        }

        Page page = pageRepositorry.findByTasks_Task(oldTask).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());

        Task task = new Task();
        if (patching) BeanUtils.copyProperties(oldTask, task);
        autoMapper.map(taskDTO, task, patching);

        keepFields(task, oldTask);
        logService.generateLog(Action.UPDATE, task, oldTask);

        if (task.getLogs().size() != oldTask.getLogs().size()) {
            notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        }
        TaskGetDTO taskGetDTO = ModelToGetDTO.tranform(taskRepository.save(task));
      
        generateGoogleCalendar(taskGetDTO);
        return taskGetDTO;

    }

    //TODO: quando ele mudar os users da task
    private void generateGoogleCalendar(TaskGetDTO task) {
        try {
            List<PropertyValueGetDTO> list = task.getProperties().stream()
                    .filter(p -> p.getProperty().getType().equals(TypeOfProperty.USER)).toList();
            List<PropertyValueGetDTO> dates = task.getProperties().stream().filter(prop ->
                    prop.getProperty().getType().equals(TypeOfProperty.DATE) &&
                            ((DateGetDTO) prop.getProperty()).getDeadline()).toList();
            Collection<OtherUsersDTO> users = list.stream().map(prop -> (Collection<OtherUsersDTO>) prop.getValue().getValue())
                    .flatMap(Collection::stream).filter(u -> {
                        User user = userRepository.findById(u.getId()).get();
                        return user.getConfiguration().getGoogleCalendar();
                    }).toList();
            Collection<PropertyValueGetDTO> valuesdates = dates.stream().map(d ->
            {
                if (d.getValue().getValue() == null) return d;
                DateWithGoogle value = dateWithGoogleRepository.findById( ((DateWithGoogle) d.getValue().getValue()).getId()).get();
                if (value.getIdGoogle().isEmpty()) {
                    notExistsInGoogle(users, task, value);
                } else {
                    alreadyExistisInGoogle(users, task, value);
                }
                return d;
            }).toList();
            ArrayList<PropertyValueGetDTO> values = new ArrayList<>(task.getProperties().stream().filter(prop ->
                    !prop.getProperty().getType().equals(TypeOfProperty.DATE) ||
                            !((DateGetDTO)prop.getProperty()).getDeadline()).toList());
            values.addAll(valuesdates);
            task.setProperties(values);
        }catch (NullPointerException ignore){
            return;
        }


    }

    private void deleteFromCalendar(Task task){
        List<PropertyValue> dates = task.getProperties().stream().filter(prop ->
                prop.getProperty().getType().equals(TypeOfProperty.DATE) &&
                        ( (Date)prop.getProperty()).getDeadline()).toList();
        dates.forEach(d ->
        {
            DateWithGoogle value = (DateWithGoogle) d.getValue().getValue();
            if (value == null) return ;
            if (!value.getIdGoogle().isEmpty()) {
                try {
                    googleCalendarService.delete(value.getIdGoogle());
                    value.setIdGoogle("");
                    dateWithGoogleRepository.save(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }






    private void alreadyExistisInGoogle(Collection<OtherUsersDTO> u, TaskGetDTO t, DateWithGoogle value) {
        try {
            googleCalendarService.update(u, t, value.getDateTime(), value.getIdGoogle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private void notExistsInGoogle(Collection<OtherUsersDTO> u, TaskGetDTO t, DateWithGoogle value) {
        try {
            System.out.println("NOT EXISTS");
            value.setIdGoogle(googleCalendarService.create(u, t, value.getDateTime()));
            dateWithGoogleRepository.save(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    //this keep the fields that can't be changed
    private void keepFields(Task task, Task oldTask) {
        task.setLogs(List.copyOf(oldTask.getLogs()));
//        task.setCompleted(false);
        task.setDeleted(false);
        task.setComments(oldTask.getComments());
        task.setProperties(propertyValueService.createNotSaved(task));
        Stream<PropertyValue> archiveProps = task.getProperties().stream().filter(p -> p.getProperty().getType().equals(TypeOfProperty.ARCHIVE));
        Stream<PropertyValue> archivePropsOld = archiveProps.map(p -> oldTask.getProperties().stream().filter(o -> o.equals(p)).findFirst().orElse(p));
        List<PropertyValue> finalProps = archivePropsOld.toList();
        ArrayList<PropertyValue> taskProps = new ArrayList<>(task.getProperties());
        taskProps.removeAll(finalProps.stream().filter(p -> task.getProperties().contains(p)).toList());
        taskProps.addAll(finalProps);
        task.setProperties(taskProps);
    }


    public void delete(Long id, Long projectId) {
        Task task = taskRepository.findById(id).get();

        if (task.getDeleted()) throw new TaskAlreadyDeletedException();
        Page page = pageRepositorry.findByTasks_Task(task).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());

        //set the attributes to delete the task
        task.setDateDeleted(OffsetDateTime.now());
        task.setDeleted(true);

        // generate logs
        logService.generateLog(Action.DELETE, task);
        taskRepository.save(task);
        deleteFromCalendar(task);

        //generate notifications
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);


    }

    public void deletePermanent(Long id, Long projectId) {
        Task task = taskRepository.findById(id).get();
        Page page = pageRepositorry.findByTasks_Task(task).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());
        taskPageRepository.deleteAll(taskPageRepository.findAllByTask_Id(id));
        taskRepository.deleteById(id);
    }

    public TaskGetDTO redo(Long id, Long projectId) {
        Task task = taskRepository.findById(id).get();
        Page page = pageRepositorry.findByTasks_Task(task).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());
        //setting the attributes to delete the task
        task.setDeleted(false);
        task.setDateDeleted(null);
        //generate  logs
        logService.generateLog(Action.REDO, task);
        TaskGetDTO tranform = ModelToGetDTO.tranform(taskRepository.save(task));

        generateGoogleCalendar(tranform);
        //generate notifications
        notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        return tranform;
    }

    public Collection<TaskGetDTO> getDeletedTasks(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        Collection<Page> pages = project.getPages();
        //this get all page's tasks of the project and filter by "deleted" attribute
        return pages.stream().map(Page::getTasks).flatMap(Collection::stream)
                .filter(t -> t.getTask().getDeleted()).map(TaskPage::getTask).map(ModelToGetDTO::tranform)
                .distinct().toList();
    }


    public TaskGetDTO complete(Long id, Long projectID) {
        Task task = taskRepository.findById(id).get();
        String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();

        if (task.getCompleted()) throw new TaskAlreadyCompleteException();
        Page page = pageRepositorry.findByTasks_Task(task).stream().findFirst().get();
        validation.ofObject(projectID, page.getProject());
        //setting attributes to complete the task
        TaskGetDTO tranform;
        if (page.getProject().getOwner().equals(user) || !page.getProject().getRevision()) {
            task.setDateCompleted(OffsetDateTime.now());
            task.setCompleted(true);
            task.setWaitingRevision(false);
          
            deleteFromCalendar(task);

            //generate the logs and notifications
            logService.generateLog(Action.COMPLETE, task);
            tranform = ModelToGetDTO.tranform(taskRepository.save(task));
            generatePointsOfComplete(task.getLogs());
            // generate notifications
            notificationService.generateNotification(TypeOfNotification.CHANGETASK, task.getId(), null);
        } else {
            task.setWaitingRevision(true);
            tranform = ModelToGetDTO.tranform(taskRepository.save(task));
        }
        return tranform;
    }

    public TaskGetDTO cancelComplete(Long id, Long projectId) {
        Task task = taskRepository.findById(id).get();
        Page page = pageRepositorry.findByTasks_Task(task).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());
        task.setWaitingRevision(false);
        return ModelToGetDTO.tranform(taskRepository.save(task));
    }

    public void generatePointsOfComplete(Collection<Log> logs) {
        // Generate the user's points based on alteration at the task
        Collection<User> users = logs.stream().map(Log::getUser).distinct().toList();
        users.forEach(u -> {
            Long qttyLogs = logs.stream().filter(l -> l.getUser().equals(u)).count();
            userService.addPoints(u, qttyLogs * 3);
        });
    }
}
