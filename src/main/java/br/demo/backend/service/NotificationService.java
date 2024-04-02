    package br.demo.backend.service;

    import br.demo.backend.model.Group;
    import br.demo.backend.model.Notification;
    import br.demo.backend.model.Project;
    import br.demo.backend.model.User;
    import br.demo.backend.model.chat.Message;
    import br.demo.backend.model.enums.TypeOfNotification;
    import br.demo.backend.model.pages.Page;
    import br.demo.backend.model.tasks.Log;
    import br.demo.backend.model.tasks.Task;
    import br.demo.backend.repository.GroupRepository;
    import br.demo.backend.repository.NotificationRepository;
    import br.demo.backend.repository.ProjectRepository;
    import br.demo.backend.repository.UserRepository;
    import br.demo.backend.repository.chat.ChatRepository;
    import br.demo.backend.repository.chat.MessageRepository;
    import br.demo.backend.repository.pages.PageRepository;
    import br.demo.backend.repository.tasks.TaskRepository;
    import lombok.AllArgsConstructor;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

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
        private MessageRepository messageRepository;

        public void generateNotification(TypeOfNotification type, Long idPrincipal, Long auxiliary){
            switch (type){
//                When someone change my permission in a project
                case CHANGEPERMISSION -> generateChangePermission(idPrincipal, auxiliary, type) ;
//                When someone add me in a group
                case ADDORREMOVEINGROUP -> generateAddInGroup(idPrincipal, auxiliary, type);
//                When someone change something in a task
                case CHANGETASK -> generateChangeTask(idPrincipal, type);
//                When someone send a message in a chat
                case CHAT -> generateChat(idPrincipal, auxiliary, type);
//                When someone comment in a task
                case COMMENTS -> generateComment(idPrincipal, auxiliary, type);
//                When some deadline is in 24 hours
                case DEADLINE -> generateDeadlineOrScheduling(idPrincipal, type);
//                When some schedule is in 24 hours
                case SCHEDULE -> generateDeadlineOrScheduling(idPrincipal, type);
//                When someone pass the target of points
                case POINTS -> generatePoints(idPrincipal, auxiliary, type);
            }
        }
        private void generateChangePermission(Long idUser, Long idProject, TypeOfNotification type ){
    //      User user  = userRepository.findById(idUser);
            //       Apagar

            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userRepository.findByUserDetailsEntity_Username(username).get();
            //      /Apagar
            //Verify if the user wants to recive a notification for this
            if(!user.getConfiguration().getNotificWhenChangeMyPermission()) return;
            Project project = projectRepository.findById(idProject).get();
            Group group = groupRepository.findGroupByPermissions_ProjectAndUsersContaining(project, user);
            notificationRepository.save(new Notification(null, "Your permission was changed at project '"+project.getName()+"'",
                    type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId()+"/group/"+group.getId(), user, false ));
        }

        private void generateAddInGroup(Long userId, Long groupId, TypeOfNotification type){
            //      User user  = userRepository.findById(idUser);
            //       Apagar

            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userRepository.findByUserDetailsEntity_Username(username).get();
            //      /Apagar
            //Verify if the user wants to recive a notification for this
            if(!user.getConfiguration().getNotificAtAddMeInAGroup()) return;
            Group group = groupRepository.findById(groupId).get();
            if(group.getUsers().contains(user)){
                notificationRepository.save(new Notification(null, "You was added at group '"+group.getName()+"'",
                        type, "/"+user.getUserDetailsEntity().getUsername()+"/group/"+groupId, user, false ));
            }else{
                notificationRepository.save(new Notification(null, "You was removed at group '"+group.getName()+"'",
                        type, "", user, false ));
            }

        }

        private void generateChangeTask(Long taskId, TypeOfNotification type){
            Task task = taskRepository.findById(taskId).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();
            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            //TODO:mudar para pegar o id do user logado
    //        users = users.stream().filter(u -> !u.getId.equals(userId)).toList();
            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals("jonatas")).forEach(user -> {
                //Verify if the user wants to recive a notification for this
                if(!user.getConfiguration().getNotificTasks()) return;
                Log lastLog = new ArrayList<>(task.getLogs()).get(task.getLogs().size()-1);
                //Specify what happend with de task.
                String message =
                switch (lastLog.getAction()){
                    case UPDATE -> "The task '"+task.getName()+"' was modified";
                    case REDO -> "The task '"+task.getName()+"' was redone";
                    case CREATE -> "A task was created";
                    case DELETE ->"The task '"+task.getName()+"' was deleted";
                    case COMPLETE -> "The task '"+task.getName()+"' was completed";
                };
                //TODO: ver como abrir o  modal de edição de task;
                notificationRepository.save(new Notification(null, message,
                        type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId(), user, false ));
            });
        }

        private void generateChat (Long messageId, Long chatId, TypeOfNotification type) {
            Message message = messageRepository.findById(messageId).get();
            message.getDestinations().forEach(destination -> {
                //Verify if the user wants to recive a notification for this
                if (!destination.getUser().getConfiguration().getNotificChats() || destination.getVisualized()) return;
                //Verify if the notification just have an annex
                if (message.getValue().isEmpty()) {
                    notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername() + " send a annex to you", type,
                            "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
                } else {
                    notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername() + " send '" + message.getValue() + "' to you", type,
                            "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
                }
            });
        }

        private void generateComment(Long idTask, Long idComment, TypeOfNotification type){
            Task task = taskRepository.findById(idTask).get();
            Message message = messageRepository.findById(idComment).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();
            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            //        users = users.stream().filter(u -> !u.getId.equals(userId)).toList();
            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals("jonatas")).forEach(user -> {
                //Verify if the user wants to recive a notification for this
                if (!user.getConfiguration().getNotificComments()) return;
                //TODO: ver como abrir o  modal de edição de task;
                notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername() + " comment in the task '"+task.getName()+"' '"+message.getValue()+"'" , type,
                        "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId(), user, false));
            });
        }

        private void generatePoints(Long idUser, Long pointsTarget , TypeOfNotification type){
            //      User user  = userRepository.findById(idUser);
            //       Apagar

            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userRepository.findByUserDetailsEntity_Username(username).get();
            //      /Apagar
            //Verify if the user wants to recive a notification for this
            if(!user.getConfiguration().getNotificMyPointsChange()) return;
            notificationRepository.save(new Notification(null, "Congrats! You pass the target of "+pointsTarget+" points", type,
                    "/"+user.getUserDetailsEntity().getUsername()+"/configurations/account", user, false));
        }

        private void generateDeadlineOrScheduling(Long idTask, TypeOfNotification type){
            Task task = taskRepository.findById(idTask).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();
            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            //        users = users.stream().filter(u -> !u.getId.equals(userId)).toList();
            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals("jonatas")).forEach(user -> {
                //Verify if the user wants to recive a notification for this
                if (TypeOfNotification.DEADLINE.equals(type) && !user.getConfiguration().getNotificDeadlines()) return;
                if (TypeOfNotification.SCHEDULE.equals(type) && !user.getConfiguration().getNotificSchedules()) return;
                notificationRepository.save(new Notification(null, "The task '"+task.getName()+"' has a "+(TypeOfNotification.SCHEDULE.equals(type) ? "schedule" : "deadline")+" in 24 hours",
                        type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId(), user, false ));
            });
        }

        public void generateDeadlineOrSchedulingInProjct(Long idProject, TypeOfNotification type){
            Project project = projectRepository.findById(idProject).get();
            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            //        users = users.stream().filter(u -> !u.getId.equals(userId)).toList();
            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals("jonatas")).forEach(user -> {
                //Verify if the user wants to recive a notification for this
                if (TypeOfNotification.DEADLINE.equals(type) && !user.getConfiguration().getNotificDeadlines()) return;
                if (TypeOfNotification.SCHEDULE.equals(type) && !user.getConfiguration().getNotificSchedules()) return;
                notificationRepository.save(new Notification(null, "The project '"+project.getName()+"' has a "+(TypeOfNotification.SCHEDULE.equals(type) ? "schedule" : "deadline")+" in 24 hours",
                        type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId(), user, false ));
            });
        }
    }
