package br.demo.backend.service;

import br.demo.backend.exception.AlreadyAceptException;
import br.demo.backend.model.*;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.*;
import br.demo.backend.repository.chat.MessageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {


    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private ProjectRepository projectRepository;
    private PageRepository pageRepository;
    private GroupRepository groupRepository;
    private MessageRepository messageRepository;
    private PermissionRepository permissionRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    public void generateNotification(TypeOfNotification type, Long idPrincipal, Long auxiliary){
        switch (type){
//                When someone change my permission in a project
            case CHANGEPERMISSION -> generateChangePermission(idPrincipal, auxiliary, type) ;
//                When someone add me in a group
            case ADDINGROUP, REMOVEINGROUP -> generateAddInGroup(idPrincipal, auxiliary, type);
//                When someone change something in a task
            case CHANGETASK -> generateChangeTask(idPrincipal, type);
//                When someone send a message in a chat
            case CHAT -> generateChat(idPrincipal, auxiliary, type);
//                When someone comment in a task
            case COMMENTS -> generateComment(idPrincipal, auxiliary, type);
//                When some deadline is in 24 hours
            case DEADLINE -> generateDeadlineOrScheduling(idPrincipal, auxiliary, type);
//                When some schedule is in 24 hours
            case SCHEDULE -> generateDeadlineOrScheduling(idPrincipal, auxiliary, type);
//                When someone pass the target of points
            case POINTS -> generatePoints(idPrincipal, auxiliary, type);
            case INVITETOPROJECT -> generateInviteProject(idPrincipal, auxiliary, type);
        }
    }

    private void generateInviteProject(Long idProject, Long idGroup, TypeOfNotification type) {
        Project project = projectRepository.findById(idProject).get();
        Group group = groupRepository.findById(idGroup).get();
        Notification notify = notificationRepository.save(new Notification(null, project.getName(), type, "/"+group.getOwner().getUserDetailsEntity().getUsername()+"/"+
                project.getId(), group.getOwner(), false,  project.getId(), group.getId()));
        simpMessagingTemplate.convertAndSend("/notifications/"+group.getOwner().getId(), notify);
    }

    private void generateChangePermission(Long idUser, Long idProject, TypeOfNotification type ){
        User user  = userRepository.findById(idUser).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return;
        Project project = projectRepository.findById(idProject).get();
        Group group = groupRepository.findGroupByPermissions_ProjectAndUsersContaining(project, user);

        Notification notify = notificationRepository.save(new Notification(null, project.getName(), type, "/"+user.getUserDetailsEntity().getUsername()+"/"+
                project.getId()+"/group/"+group.getId(), user, false, group.getId(), project.getId()));
        simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
    }

    private void generateAddInGroup(Long userId, Long groupId, TypeOfNotification type){
        User user  = userRepository.findById(userId).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return;

        Group group = groupRepository.findById(groupId).get();
        Notification notify = notificationRepository.save(new Notification(null, group.getName(),
                type, type == TypeOfNotification.ADDINGROUP ? "/"+
                user.getUserDetailsEntity().getUsername()+"/group/"+groupId : "",
                user, false, null, group.getId()));
        simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);

    }


    private void generateChangeTask(Long taskId, TypeOfNotification type) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Task task = taskRepository.findById(taskId).get();
        Page page = pageRepository.findByTasks_Task(task).stream().findFirst().get();
        Project project = page.getProject();

        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());

        users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals(username)).forEach(user -> {
            if (!verifyIfHeWantsThisNotification(type, user)) return;
            Notification notify = notificationRepository.save(new Notification(null, task.getName(),
                    type, "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId() +
                    "/" + page.getId(), user, false, null,taskId));
            simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
        });
    }

    private void generateChat (Long messageId, Long chatId, TypeOfNotification type) {
        Message message = messageRepository.findById(messageId).get();
        message.getDestinations().forEach(destination -> {
            if (!verifyIfHeWantsThisNotification(type,destination.getUser())) return;
            //Verify if the notification just have an annex
            if (message.getValue().isEmpty()) {
                Notification notify = notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername(), type,
                        "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false,  message.getSender().getId(), chatId));
                simpMessagingTemplate.convertAndSend("/notifications/"+destination.getUser().getId(), notify);
            } else {
                Notification notify = notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername(), type,
                        "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false,  message.getSender().getId(), chatId));
                simpMessagingTemplate.convertAndSend("/notifications/"+destination.getUser().getId(), notify);
            }
        });
    }

    private void generateComment(Long idTask, Long idComment, TypeOfNotification type){
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Task task = taskRepository.findById(idTask).get();
        Message message = messageRepository.findById(idComment).get();
        Page page = pageRepository.findByTasks_Task(task).stream().findFirst().get();
        Project project = page.getProject();

        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());
        users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals(username)).forEach(user -> {
            if (!verifyIfHeWantsThisNotification(type, user)) return;
            Notification notify = notificationRepository.save(new Notification(null, task.getName() , type,
                    "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId(), user, false, message.getId(), idTask));
            simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
        });
    }

    private void generatePoints(Long idUser, Long pointsTarget , TypeOfNotification type){
        User user  = userRepository.findById(idUser).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return;
        Notification notify = notificationRepository.save(new Notification(null, pointsTarget.toString(), type,
                "/"+user.getUserDetailsEntity().getUsername()+"/configurations/account",
                user, false, user.getPoints(), user.getId()));
        simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
    }

    //typeObj: 0 = task; 1 = project
    private void generateDeadlineOrScheduling(Long idObj, Long typeObj ,TypeOfNotification type){

        if(typeObj == 0){
            Task task = taskRepository.findById(idObj).get();
            Page page = pageRepository.findByTasks_Task(task).stream().findFirst().get();
            Project project = page.getProject();
            generateForEachUserDeadlineAndSchedule(type, typeObj, project, task, page);
        }else{
            Project project = projectRepository.findById(idObj).get();
            generateForEachUserDeadlineAndSchedule(type, typeObj, project, null, null);
        }
    }

    private void generateForEachUserDeadlineAndSchedule(TypeOfNotification type, Long typeObj,
                                                        Project project, Task task, Page page){
        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());
        users.forEach(user -> {
            if (!verifyIfHeWantsThisNotification(type, user)) return;
            if(typeObj == 0){
                generateInTaskNotification(user, type, project,task, page);
            }else{
                generateInProjectNotification(project,type,user);
            }
        });
    }

    public  void generateInProjectNotification(Project project, TypeOfNotification type, User user){
        Notification notify = notificationRepository.save(new Notification(null, project.getName(),
                type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId(), user, false, null, project.getId() ));
        simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
    }
    public void generateInTaskNotification(User user, TypeOfNotification type, Project project, Task task, Page page){
        Notification notify = notificationRepository.save(new Notification(null, task.getName(),
                type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId()+"/"+task.getId(), user, false, null, task.getId() ));
        simpMessagingTemplate.convertAndSend("/notifications/"+user.getId(), notify);
    }


    private Boolean verifyIfHeWantsThisNotification(TypeOfNotification type, User user){
        Configuration config  = user.getConfiguration();
        if(!config.getNotifications() &&
                !List.of(TypeOfNotification.ADDINGROUP, TypeOfNotification.INVITETOPROJECT).contains(type)) return false;
        return switch (type){
            case INVITETOPROJECT -> true;
            case ADDINGROUP -> true;
            case REMOVEINGROUP ->  config.getNotificAtAddMeInAGroup();
            case CHANGEPERMISSION ->  config.getNotificWhenChangeMyPermission();
            case CHANGETASK ->  config.getNotificTasks();
            case CHAT ->  config.getNotificChats();
            case POINTS ->  config.getNotificMyPointsChange();
            case DEADLINE ->  config.getNotificDeadlines();
            case SCHEDULE ->  config.getNotificSchedules();
            case COMMENTS ->  config.getNotificComments();
        };
    }

    public Collection<Notification> visualize(){
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        Collection<Notification> notifications = notificationRepository.findAllByUser(user);
        return notifications.stream().map(n -> {
            n.setVisualized(true);
            notificationRepository.save(n);
            return n;
        }).toList();
    }
    public void deleteNotification(Long id){
        notificationRepository.deleteById(id);
    }

    public void click(Long id) {
        Notification notification = notificationRepository.findById(id).get();
        notificationRepository.deleteById(id);
        if(notification.getType().equals(TypeOfNotification.ADDINGROUP)){
            Group group = groupRepository.findById(notification.getObjId()).get();
            User user = notification.getUser();
            testAlreadyAceptToGroup(group, user);
            group.getUsers().add(user);
            user.getPermissions().addAll(group.getPermissions());
            groupRepository.save(group);
            userRepository.save(user);
        } else if(notification.getType().equals(TypeOfNotification.INVITETOPROJECT)){
            Group group = groupRepository.findById(notification.getObjId()).get();
            Project project = projectRepository.findById(notification.getAuxObjId()).get();
            testAlreadyAceptToProject(group, project);
            Permission permission = permissionRepository.findByProjectAndIsDefault(project, true);
            group.getPermissions().add(permission);
            Collection<User> users = groupRepository.save(group).getUsers();
            users.forEach(u -> {
                u.getPermissions().add(permission);
                userRepository.save(u);
            });
        }
    }

    private void testAlreadyAceptToGroup(Group group, User user){
        if(group.getUsers().contains(user)) throw new AlreadyAceptException();
    }
    private void testAlreadyAceptToProject(Group group, Project project){
        if(group.getPermissions().stream().anyMatch(p -> p.getProject().equals(project))) throw new AlreadyAceptException();
    }


    public void updateNotification(Notification notification){
        notificationRepository.save(notification);
    }
}