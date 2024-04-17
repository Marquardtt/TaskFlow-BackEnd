    package br.demo.backend.service;

    import br.demo.backend.model.*;
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
                case DEADLINE -> generateDeadlineOrScheduling(idPrincipal, auxiliary, type);
//                When some schedule is in 24 hours
                case SCHEDULE -> generateDeadlineOrScheduling(idPrincipal, auxiliary, type);
//                When someone pass the target of points
                case POINTS -> generatePoints(idPrincipal, auxiliary, type);
            }
        }
        private void generateChangePermission(Long idUser, Long idProject, TypeOfNotification type ){
            User user  = userRepository.findById(idUser).get();
            if (!verifyIfHeWantsThisNotification(type, user)) return;
            Project project = projectRepository.findById(idProject).get();
            Group group = groupRepository.findGroupByPermissions_ProjectAndUsersContaining(project, user);

            notificationRepository.save(new Notification(null, project.getName(), type, "/"+user.getUserDetailsEntity().getUsername()+"/"+
                    project.getId()+"/group/"+group.getId(), user, false ));
        }

        private void generateAddInGroup(Long userId, Long groupId, TypeOfNotification type){
            User user  = userRepository.findById(userId).get();
            if (!verifyIfHeWantsThisNotification(type, user)) return;

            Group group = groupRepository.findById(groupId).get();
            if(group.getUsers().contains(user)){
                notificationRepository.save(new Notification(null, group.getName(),
                        type, "/"+user.getUserDetailsEntity().getUsername()+"/group/"+groupId, user, false ));
            }else{
                notificationRepository.save(new Notification(null, group.getName(),
                        type, "", user, false ));
            }

        }

        private void generateChangeTask(Long taskId, TypeOfNotification type) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

            Task task = taskRepository.findById(taskId).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();

            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            users.add(project.getOwner());

            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals(username)).forEach(user -> {
                if (!verifyIfHeWantsThisNotification(type, user)) return;
                notificationRepository.save(new Notification(null, task.getName(),
                        type, "/" + user.getUserDetailsEntity().getUsername() + "/" + project.getId() +
                        "/" + page.getId() + "/" + taskId, user, false));
            });
        }

        private void generateChat (Long messageId, Long chatId, TypeOfNotification type) {
            Message message = messageRepository.findById(messageId).get();
            message.getDestinations().forEach(destination -> {
                if (!verifyIfHeWantsThisNotification(type,destination.getUser())) return;
                //Verify if the notification just have an annex
                if (message.getValue().isEmpty()) {
                    notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername(), type,
                            "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
                } else {
                    notificationRepository.save(new Notification(null, message.getSender().getUserDetailsEntity().getUsername(), type,
                            "/" + destination.getUser().getUserDetailsEntity().getUsername() + "/chat/" + chatId, destination.getUser(), false));
                }
            });
        }

        private void generateComment(Long idTask, Long idComment, TypeOfNotification type){
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

            Task task = taskRepository.findById(idTask).get();
            Message message = messageRepository.findById(idComment).get();
            Page page = pageRepository.findByTasks_Task(task);
            Project project = page.getProject();

            Collection<User> users = userRepository.findAllByPermissions_Project(project);
            users.add(project.getOwner());
            users.stream().filter(u -> !u.getUserDetailsEntity().getUsername().equals(username)).forEach(user -> {
                if (!verifyIfHeWantsThisNotification(type, user)) return;
                notificationRepository.save(new Notification(null, task.getName() , type,
                        "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId()+"/"+idTask, user, false));
            });
        }

        private void generatePoints(Long idUser, Long pointsTarget , TypeOfNotification type){
            User user  = userRepository.findById(idUser).get();
            if (!verifyIfHeWantsThisNotification(type, user)) return;
            notificationRepository.save(new Notification(null, pointsTarget.toString(), type,
                    "/"+user.getUserDetailsEntity().getUsername()+"/configurations/account", user, false));
        }

        //typeObj: 0 = task; 1 = project
        private void generateDeadlineOrScheduling(Long idObj, Long typeObj ,TypeOfNotification type){

            if(typeObj == 0){
                Task task = taskRepository.findById(idObj).get();
                Page page = pageRepository.findByTasks_Task(task);
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
            notificationRepository.save(new Notification(null, project.getName(),
                    type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId(), user, false ));
        }
        public void generateInTaskNotification(User user, TypeOfNotification type, Project project, Task task, Page page){
            notificationRepository.save(new Notification(null, task.getName(),
                    type, "/"+user.getUserDetailsEntity().getUsername()+"/"+project.getId() +"/"+page.getId()+"/"+task.getId(), user, false ));
        }


        private Boolean verifyIfHeWantsThisNotification(TypeOfNotification type, User user){
            Configuration config  = user.getConfiguration();
            if(!config.getNotifications()) return false;
            return switch (type){
                case ADDORREMOVEINGROUP ->  config.getNotificAtAddMeInAGroup();
                case CHANGEPERMISSION ->  config.getNotificWhenChangeMyPermission();
                case CHANGETASK ->  config.getNotificTasks();
                case CHAT ->  config.getNotificChats();
                case POINTS ->  config.getNotificMyPointsChange();
                case DEADLINE ->  config.getNotificDeadlines();
                case SCHEDULE ->  config.getNotificSchedules();
                case COMMENTS ->  config.getNotificComments();
            };
        }

        public void updateNotification(Notification notification){
            notificationRepository.save(notification);
        }
    }
