package br.demo.backend.controller;

import br.demo.backend.model.Notification;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    @PatchMapping("/visualize")
    public Collection<Notification> visualizeNotification(){
        return notificationService.visualize();
    }

    @PatchMapping("/click/{id}")
    public void clickNotification(@PathVariable Long id){
         notificationService.click(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id){
         notificationService.deleteNotification(id);
    }

}
