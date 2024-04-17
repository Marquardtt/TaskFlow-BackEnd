package br.demo.backend.service;

import br.demo.backend.model.*;
import br.demo.backend.model.chat.Destination;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.NotificationRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.chat.MessageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.websocket.MyHandle;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class NotificationService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private ProjectRepository projectRepository;
    private PageRepository pageRepository;
    private GroupRepository groupRepository;
    private SimpMessagingTemplate simpMessagingTemplate;
    private MessageRepository messageRepository;

    public void generateNotification(TypeOfNotification type, Long idPrincipal, Long auxiliary) {
        ArrayList<Object> returns = switch (type) {
//                When someone change my permission in a project
            case CHANGEPERMISSION -> generateChangePermission(idPrincipal, auxiliary, type);
//                When someone add me in a group
            case ADDORREMOVEINGROUP -> generateAddInGroup(idPrincipal, auxiliary, type);
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
        };
        simpMessagingTemplate.convertAndSend("/notification/" + ((User) returns.get(0)).getId(), returns.get(1));
    }

    private ArrayList<Object> generateChangePermission(Long idUser, Long idProject, TypeOfNotification type) {
        User user = userRepository.findById(idUser).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return null;

        Project project = projectRepository.findById(idProject).get();
        Group group = groupRepository.findGroupByPermissions_ProjectAndUsersContaining(project, user);
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(user);
        list.add(notificationRepository.save(new Notification(null, "Your permission was changed at project '" +
                project.getName() + "'", type, "/" + user.getUserDetailsEntity().getUsername() + "/" +
                project.getId() + "/group/" + group.getId(), user, false)));
        return list;

    }

    private ArrayList<Object> generateAddInGroup(Long userId, Long groupId, TypeOfNotification type) {
        User user = userRepository.findById(userId).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return null;
        Group group = groupRepository.findById(groupId).get();
        Notification notification;
        if (group.getUsers().contains(user)) {
            notification = notificationRepository.save(new Notification(null, "You was added at group '" + group.getName() + "'",
                    type, "/" + user.getUserDetailsEntity().getUsername() + "/group/" + groupId, user, false));
        } else {
            notification = notificationRepository.save(new Notification(null, "You was removed at group '" + group.getName() + "'",
                    type, "", user, false));
        }
        ArrayList<Object> list = new ArrayList<>();
        list.add(user);
        list.add(notification);
        return list;
    }

    private ArrayList<Object> generateChangeTask(Long taskId, TypeOfNotification type) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Task task = taskRepository.findById(taskId).get();
        Page page = pageRepository.findByTasks_Task(task);
        Project project = page.getProject();

        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());
        ArrayList<Object> list = new ArrayList<>();

        for (User user : users) {
            if (!user.getUserDetailsEntity().getUsername().equals(username)) {
                list.add(sendNotification(type, user, task, project, page));
                list.add(user);
                return list;
            }
        }
        return null;
    }

    private Notification sendNotification(TypeOfNotification type, User user, Task task, Project project, Page page) {
        if (!verifyIfHeWantsThisNotification(type, user)) return null;
        String message = getMessageTask(task);

        return notificationRepository.save(new Notification(null, message,
                type, "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId() +
                "/" + page.getId() + "/" + task.getId(), user, false));
    }

    private static String getMessageTask(Task task) {
        Log lastLog = new ArrayList<>(task.getLogs()).get(task.getLogs().size() - 1);
        //Specify what happend with de task.
        String message =
                switch (lastLog.getAction()) {
                    case UPDATE -> "The task '" + task.getName() + "' was modified";
                    case REDO -> "The task '" + task.getName() + "' was redone";
                    case CREATE -> "A task was created";
                    case DELETE -> "The task '" + task.getName() + "' was deleted";
                    case COMPLETE -> "The task '" + task.getName() + "' was completed";
                };
        return message;
    }

    private ArrayList<Object> generateChat(Long messageId, Long chatId, TypeOfNotification type) {
        Message message = messageRepository.findById(messageId).get();

        for (Destination destination : message.getDestinations()) {

            ArrayList<Object> list = new ArrayList<>();
            Notification notification;
            if (!verifyIfHeWantsThisNotification(type, destination.getUser())) return null;
            //Verify if the notification just have an annex
            if (message.getValue().isEmpty()) {
                notification = notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername() + " send a annex to you", type,
                        "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
            } else {
                notification = notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername() + " send '" + message.getValue() + "' to you", type,
                        "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
            }
            list.add(destination.getUser());
            list.add(notification);
            return list;
        }
        return null;
    }

    private ArrayList<Object> generateComment(Long idTask, Long idComment, TypeOfNotification type) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Task task = taskRepository.findById(idTask).get();
        Message message = messageRepository.findById(idComment).get();
        Page page = pageRepository.findByTasks_Task(task);
        Project project = page.getProject();

        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());
        Notification notification;
        ArrayList<Object> list = new ArrayList<>();

        for (User user : users) {
            if (!user.getUserDetailsEntity().getUsername().equals(username)) {
                if (!verifyIfHeWantsThisNotification(type, user)) continue;
               notification = notificationRepository.save(new Notification(
                        null,
                        message.getSender().getUserDetailsEntity().getUsername() + " comment in the task '" + task.getName() + "' '" + message.getValue() + "'",
                        type,
                        "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId() + "/" + page.getId() + "/" + idTask,
                        user,
                        false
                ));
               list.add(user);
               list.add(notification);
               return list;
            }
        }
        return null;
    }

    private ArrayList<Object> generatePoints(Long idUser, Long pointsTarget, TypeOfNotification type) {

        User user = userRepository.findById(idUser).get();
        if (!verifyIfHeWantsThisNotification(type, user)) return null;
        ArrayList<Object> list = new ArrayList<>();
        list.add(user);
        list.add(notificationRepository.save(new Notification(null, "Congrats! You pass the target of " + pointsTarget + " points", type,
                "/" + user.getUserDetailsEntity().getUsername() + "/configurations/account", user, false)));
        return list;

    }

    //typeObj: 0 = task; 1 = project
    private ArrayList<Object> generateDeadlineOrScheduling(Long idObj, Long typeObj, TypeOfNotification type) {

        if (typeObj == 0) {
            Task task = taskRepository.findById(idObj).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();
           return generateForEachUserDeadlineAndSchedule(type, typeObj, project, task, page);
        } else {
            Project project = projectRepository.findById(idObj).get();
            return generateForEachUserDeadlineAndSchedule(type, typeObj, project, null, null);
        }
    }

    private ArrayList<Object> generateForEachUserDeadlineAndSchedule(TypeOfNotification type, Long typeObj,
                                                                     Project project, Task task, Page page) {
        Collection<User> users = userRepository.findAllByPermissions_Project(project);
        users.add(project.getOwner());
        ArrayList<Object> list = new ArrayList<>();

        for (User user : users){
            if (!verifyIfHeWantsThisNotification(type, user)) return null;
            if (typeObj == 0) {
                list = generateInTaskNotification(user, type, project, task, page);
            } else {
               list = generateInProjectNotification(project, type, user);
            }
        }
        return list;
    }

    public ArrayList<Object> generateInProjectNotification(Project project, TypeOfNotification type, User user) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(user);
        list.add(notificationRepository.save(new Notification(null, "The project '" + project.getName() + "' has a " + (TypeOfNotification.SCHEDULE.equals(type) ? "schedule" : "deadline") + " in 24 hours",
                type, "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId(), user, false)));
        return list;
    }

    public ArrayList<Object> generateInTaskNotification(User user, TypeOfNotification type, Project project, Task task, Page page) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(user);
        list.add(notificationRepository.save(new Notification(null, "The task '" + task.getName() + "' has a " + (TypeOfNotification.SCHEDULE.equals(type) ? "schedule" : "deadline") + " in 24 hours",
                type, "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId() + "/" + page.getId() + "/" + task.getId(), user, false)));
        return list;
    }

    private Boolean verifyIfHeWantsThisNotification(TypeOfNotification type, User user) {
        Configuration config = user.getConfiguration();
        if (!config.getNotifications()) return false;
        return switch (type) {
            case ADDORREMOVEINGROUP -> config.getNotificAtAddMeInAGroup();
            case CHANGEPERMISSION -> config.getNotificWhenChangeMyPermission();
            case CHANGETASK -> config.getNotificTasks();
            case CHAT -> config.getNotificChats();
            case POINTS -> config.getNotificMyPointsChange();
            case DEADLINE -> config.getNotificDeadlines();
            case SCHEDULE -> config.getNotificSchedules();
            case COMMENTS -> config.getNotificComments();
        };
    }

    public void updateNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
