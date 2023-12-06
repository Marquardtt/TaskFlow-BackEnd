package br.demo.backend.service;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;

import java.util.Collection;

public class ResolveStackOverflow {

    public static void resolveStackOverflow(Project project) {
        try {
            for (Page page : project.getPages()) {
                resolveStackOverflow(page);
            }
            for (Property property : project.getProperties()) {
                resolveStackOverflow(property);
            }
            resolveStackOverflow(project.getOwner());
        } catch (NullPointerException ignored) {
        }
    }

    public static void resolveStackOverflow(Page page) {
        try {
            for (Property property : page.getProperties()) {
                resolveStackOverflow(property);
            }
            page.setProject(null);
            if (page instanceof Canvas) {
                for (TaskCanvas taskCanvas : ((Canvas) page).getTasks()) {
                    resolveStackOverflow(taskCanvas.getTask());
                }
            } else {
                for (Task task : ((CommonPage) page).getTasks()) {
                    resolveStackOverflow(task);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void resolveStackOverflow(Property property) {
        property.setPages(null);
        property.setProject(null);
    }

    public static void resolveStackOverflow(Group group) {
        try {
            for (User user : group.getUsers()) {
                resolveStackOverflow(user);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void resolveStackOverflow(Task task) {
        try {
            for (TaskValue taskValue : task.getProperties()) {
                if(taskValue.getProperty().getType().equals(TypeOfProperty.USER)){
                    for(User user : (Collection<User>)taskValue.getValue()){
                        resolveStackOverflow(user);
                    }
                }
                resolveStackOverflow(taskValue.getProperty());
            }
            for (Message message : task.getComments()) {
                message.getUser().setPermission(null);
            }
            for (Log log : task.getLogs()) {
                log.getUser().setPermission(null);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void resolveStackOverflow(User user) {
        try {
            for (Permission permission : user.getPermission()) {
                resolveStackOverflow(permission.getProject());
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static void resolveStackOverflow(Chat chat) {
        try {
            for (User user : chat.getUsers()) {
                resolveStackOverflow(user);
            }
            for (Message message : chat.getMessages()) {
                resolveStackOverflow(message.getUser());
            }
            resolveStackOverflow(chat.getLastMessage().getUser());
        } catch (NullPointerException ignored) {
        }
    }
}
