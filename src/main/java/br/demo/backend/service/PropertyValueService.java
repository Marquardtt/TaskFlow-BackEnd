package br.demo.backend.service;

import br.demo.backend.interfaces.IHasProperties;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.repository.relations.PropertyValueRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.repository.values.ArchiveValuedRepository;
import br.demo.backend.repository.values.UserValuedRepository;
import br.demo.backend.utils.IdProjectValidation;
import br.demo.backend.utils.ModelToGetDTO;
import com.google.api.services.calendar.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PropertyValueService {

    UserRepository userRepository;
    UserValuedRepository userValuedRepository;
    TaskRepository taskRepository;
    private IdProjectValidation validation;
    PropertyValueRepository propertyValueRepository;
    ProjectRepository projectRepository;
    ArchiveValuedRepository archiveValuedRepository;
    private PageRepository pageRepository;
    private LimitedRepository limitedRepository;
    private DateRepository dateRepository;
    private SelectRepository selectRepository;
    private LogService logService;

    public Collection<PropertyValue> keepPropertyValues(Task task, Task oldTask) {
        return task.getProperties().stream().peek(
                p -> {
                    PropertyValue prVlOld = oldTask.getProperties().stream()
                            .filter(pOld -> p.getId() != null && p.getId().equals(pOld.getId())).findFirst().orElse(null);
                    if (prVlOld != null && p.getProperty().getType().equals(TypeOfProperty.ARCHIVE)) {
                        p.getValue().setValue(prVlOld.getValue().getValue());
                    }
                }
        ).toList();
    }

    public void setTaskProperties(Collection<Property> properties, Task taskEmpty) {
        Collection<PropertyValue> propertyValues = new ArrayList<>(properties.stream().map(this::setTaskProperty).toList());
        propertyValues.addAll(taskEmpty.getProperties());
        taskEmpty.setProperties(propertyValues);
    }

    public PropertyValue setTaskProperty(Property p) {
        Value value = switch (p.getType()) {
            case RADIO, SELECT -> new UniOptionValued();
            case CHECKBOX, TAG -> new MultiOptionValued();
            case TEXT -> new TextValued();
            case DATE -> new DateValued();
            case NUMBER, PROGRESS -> new NumberValued();
            case TIME -> new TimeValued();
            case ARCHIVE -> new ArchiveValued();
            case USER -> new UserValued();
        };
        return new PropertyValue(null, p, value);
    }

    public Collection<TaskGetDTO> getTasksToday(OffsetDateTime date) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        //get the values that contains the logged user
        return getTaskByUser(date, user);
    }


    public List<TaskGetDTO> getTaskByUser(OffsetDateTime date, User user) {

        Collection<Value> values = userValuedRepository.findAllByUsersContaining(user);
        //get the property values that contains the values
        Collection<PropertyValue> taskValues =
                values.stream().map(v -> propertyValueRepository
                                .findByProperty_TypeAndValue(TypeOfProperty.USER, v))
                        .toList();
        //get the tasks that contains the propertyvalues
        Collection<Task> tasks = taskValues.stream().map(tVl -> taskRepository.findByPropertiesContaining(tVl)).filter(Objects::nonNull).toList();
        return tasks.stream().filter(t ->
                        !t.getCompleted() &&
                                t.getProperties().stream().anyMatch(p ->
                                        testIfIsTodayBasesInConfigs(p, user, date)))
                .map(ModelToGetDTO::tranform).toList();
    }

    private Boolean testIfIsTodayBasesInConfigs(PropertyValue p, User user, OffsetDateTime date) {
        if (p.getProperty() instanceof Date property) {
            Boolean deadlineOrScheduling;
            if (user.getConfiguration().getInitialPageTasksPerDeadline()) {
                deadlineOrScheduling = property.getDeadline();
            } else {
                deadlineOrScheduling = property.getScheduling();
            }
            return deadlineOrScheduling && compareToThisDay(((DateWithGoogle) p.getValue().getValue()).getDateTime(), date);
        }
        return false;
    }

    private Boolean compareToThisDay(OffsetDateTime time, OffsetDateTime date) {
        try {
            if (date == null) return true;
            return time.getMonthValue() == date.getMonthValue() &&
                    time.getYear() == date.getYear() &&
                    time.getDayOfMonth() == date.getDayOfMonth();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public ArchiveValued setArchived(MultipartFile file, Long id, Boolean isInProject, Long idProject) {
        ArchiveValued archiveValued = archiveValuedRepository.findById(id).get();
        verifyConsistance(archiveValued, isInProject, idProject);
        Archive archive = null;
        if (file != null) {
            archive = new Archive(file);
        }
        archiveValued.setValue(archive);
        PropertyValue propertyValue = propertyValueRepository.findByProperty_TypeAndValue(TypeOfProperty.ARCHIVE, archiveValued);
        ILogged iLogged;
        if (isInProject) {
            iLogged = projectRepository.findByValuesContaining(propertyValue);
        } else {
            iLogged = taskRepository.findByPropertiesContaining(propertyValue);
        }
        logService.updateLogsArchive(iLogged, propertyValue);
        return archiveValuedRepository.save(archiveValued);
    }

    private void verifyConsistance(ArchiveValued archiveValued, Boolean isInProject, Long idProject) {
        PropertyValue prop = propertyValueRepository.findByProperty_TypeAndValue(TypeOfProperty.ARCHIVE, archiveValued);
        Task task = taskRepository.findByPropertiesContaining(prop);
        if (isInProject){
            Project project = projectRepository.findById(idProject).get();
            validation.ofObject(idProject, project);
        } else{
            Page page = pageRepository.findByTasks_Task(task).stream().findFirst().get();
            validation.ofObject(idProject, page.getProject());
        }
        if (taskRepository.findByPropertiesContaining(prop) != null && isInProject) {
            throw new IllegalArgumentException("The propertyvalue is on the task and not at the project");
        } else if (projectRepository.findByValuesContaining(prop) != null && !isInProject) {
            throw new IllegalArgumentException("The propertyvalue is on the project and not at the task");
        }
    }

    public Collection<PropertyValue> createNotSaved(ILogged task) {
        return task.getPropertiesValues().stream().map(p -> {
            if (p.getProperty().getId() == null) {
                Property property;
                if (p.getProperty() instanceof Limited prop) {
                    property = limitedRepository.save(prop);
                } else if (p.getProperty() instanceof Date prop) {
                    property = dateRepository.save(prop);
                } else {
                    property = selectRepository.save((Select) p.getProperty());
                }
                p.setProperty(property);
            }
            return p;
        }).toList();
    }

}
