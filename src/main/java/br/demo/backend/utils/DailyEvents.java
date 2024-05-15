package br.demo.backend.utils;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.DateValued;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.service.NotificationService;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
@EnableScheduling
public class DailyEvents {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private NotificationService notificationService;
    private UserRepository userRepository;
    private TaskService taskService;
    private PageRepository pageRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
//    Check every UTC 00:00 properties like scheduling and deadlines
    public void checkDaily() {
        usersRules();
        tasksRules();
        projectsRules();
    }

    @Transactional
    public void projectsRules() {
        //generate the notifications of deadlines e schedulings on a project
        projectRepository.findAll().forEach(this::generateNotificationsDates);
    }

    @Transactional
    public void tasksRules() {
        //generate the notifications of deadlines e schedulings on a task
        //and test if the task was deleted
        taskRepository.findAll().forEach(task -> {
            if (task.getDeleted()) {
                System.out.println("DELETED TASK");
                deleteTask(task);
                return;
            } else if (task.getCompleted()) return;
            generateNotificationsDates(task);
        });
    }

    private void usersRules() {
        System.out.println("USERRULES");
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

    private Boolean checkIfIsMoreThan30Days(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(30));
    }

    private Boolean checkIfIsMoreThan6Months(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(30 * 6));
    }


    private void deleteTask(Task task) {
        if (checkIfIsMoreThan30Days(task.getDateDeleted())) {
            Page page = pageRepository.findByTasks_Task(task).stream().findFirst().get();
            taskService.deletePermanent(task.getId(), page.getProject().getId() );
        }
    }

    @Transactional
    public void generateNotificationsDates(ILogged obj) {
        System.out.println("EXECUTANDO - NOTIFY DATES");
        obj.getPropertiesValues().forEach(property -> {
            if(!property.getProperty().getType().equals(TypeOfProperty.DATE)) return;
            if(property.getValue().getValue() == null) return;
            if(checkIfIsIn24Hours((LocalDateTime)property.getValue().getValue() )){
                if (((Date)property.getProperty()).getScheduling()) {
                    System.out.println("SCHEDULE");
                    notificationService.generateNotification(TypeOfNotification.SCHEDULE,

                            obj.getId(), obj instanceof Task ? 0L : 1L);
                }
                if (((Date)property.getProperty()).getDeadline()) {
                    System.out.println("DEADLINE");
                    notificationService.generateNotification(TypeOfNotification.DEADLINE,

                            obj.getId(), obj instanceof Task ? 0L : 1L);
                }
            }
        });
    }
}