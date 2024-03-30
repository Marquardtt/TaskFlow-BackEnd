package br.demo.backend.utils;

import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class DailyEvents {

    private TaskRepository taskRepository;
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *")
//    Check every UTC 00:00 properties like scheduling and deadlines
    public void checkDaily() {
        taskRepository.findAll().forEach(task -> {
            if(task.getDeleted()){
                if(checkIfIsMoreThan30Days(task.getDateDeleted())){
                    taskRepository.delete(task);
                }
                return;
            }else if(task.getCompleted()) return;
            task.getProperties().forEach(property -> {
                if(checkIfIsSchedulingDate(property.getProperty())){
                    if(checkIfIsIn24Hours((LocalDateTime) property.getValue().getValue())){
                        notificationService.generateNotification(TypeOfNotification.SCHEDULE, task.getId(), property.getId());
                    }
                }
                if(checkIfIsDeadlineDate(property.getProperty())){
                    if(checkIfIsIn24Hours((LocalDateTime) property.getValue().getValue())){
                        notificationService.generateNotification(TypeOfNotification.DEADLINE, task.getId(), property.getId());
                    }
                }
            });
        });

    }
    private Boolean checkIfIsIn24Hours(LocalDateTime date){
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(1)) && date.isAfter(currentDate);
    }

    private Boolean checkIfIsSchedulingDate(Property property){
        if(property.getType().equals(TypeOfProperty.DATE)){
            return ((Date) property).getScheduling();
        }
        return false;
    }
    private Boolean checkIfIsMoreThan30Days(LocalDateTime date){
        LocalDateTime currentDate = LocalDateTime.now();
        return date.isBefore(currentDate.plusDays(30));
    }
    private Boolean checkIfIsDeadlineDate(Property property){
        if(property.getType().equals(TypeOfProperty.DATE)){
            return ((Date) property).getDeadline();
        }
        return false;
    }
    //TODO: fazer para apagar a conta do usuario para sempre e trocar de senha tambem;



}