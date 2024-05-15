package br.demo.backend.utils;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class DailyEvents {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private NotificationService notificationService;
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
//    Check every UTC 00:00 properties like scheduling and deadlines
    public void checkDaily() {
        usersRules();
        tasksRules();
        projectsRules();
    }

    private void projectsRules() {
        //generate the notifications of deadlines e schedulings on a project
        projectRepository.findAll().forEach(this::generateNotificationsDates);
    }

    private void tasksRules() {
        //generate the notifications of deadlines e schedulings on a task
        //and test if the task was deleted
        taskRepository.findAll().forEach(task -> {
            if (task.getDeleted()) {
                deleteTask(task);
                return;
            } else if (task.getCompleted()) return;
            generateNotificationsDates(task);
        });
    }

    private void usersRules() {
        Collection<User> users = userRepository.findAll();
        //that delete the user that don't try to log before 30 days of his deletion
        users.stream().filter(u -> !u.getUserDetailsEntity().isEnabled() &&
                        checkIfIsMoreThan30Days(u.getUserDetailsEntity().getWhenHeTryDelete())).
                forEach(u -> userRepository.deleteById(u.getId()));
        //that make the user change the password after 6 months
        users.stream().filter(u -> checkIfIsMoreThan6Months(u.getUserDetailsEntity()
                .getLastPasswordEdition())).forEach(
                u -> {
                    u.getUserDetailsEntity().setAccountNonExpired(false);
                    userRepository.save(u);
                }
        );
    }

    private Boolean checkIfIsIn24Hours(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(1)) && date.isAfter(currentDate);
    }

    private Boolean checkIfIsSchedulingDate(Property property) {
        if (property.getType().equals(TypeOfProperty.DATE)) {
            return ((Date) property).getScheduling();
        }
        return false;
    }

    private Boolean checkIfIsMoreThan30Days(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(30));
    }

    private Boolean checkIfIsMoreThan6Months(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(30 * 6));
    }

    private Boolean checkIfIsDeadlineDate(Property property) {
        if (property.getType().equals(TypeOfProperty.DATE)) {
            return ((Date) property).getDeadline();
        }
        return false;
    }

    private void deleteTask(Task task) {
        if (checkIfIsMoreThan30Days(task.getDateDeleted())) {
            taskRepository.delete(task);
        }
    }

    private void generateNotificationsDates(ILogged obj) {
        obj.getPropertiesValues().forEach(property -> {
            if(checkIfIsIn24Hours((LocalDateTime)property.getValue().getValue() )){
                if (checkIfIsSchedulingDate(property.getProperty())) {
                    notificationService.generateNotification(TypeOfNotification.SCHEDULE,
                            obj.getId(), property.getId());
                    System.out.println("SCHEDULEEE");
                }
                if (checkIfIsDeadlineDate(property.getProperty())) {
                    notificationService.generateNotification(TypeOfNotification.DEADLINE,
                            obj.getId(), property.getId());
                    System.out.println("DEADLINEEE");
                }
            }
        });
    }
}