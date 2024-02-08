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
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.UserValued;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;

public class ResolveStackOverflow {

    public static Project resolveStackOverflow(Project project) {
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
        return project;
    }

    public static Page resolveStackOverflow(Page page) {
        try {
            for (Property property : page.getProperties()) {
                resolveStackOverflow(property);
            }
        } catch (NullPointerException ignore) {
        }
        page.setProject(null);
        try {
            if (page instanceof CommonPage) {
                resolveStackOverflow(((CommonPage) page).getPropertyOrdering());
            }
        } catch (NullPointerException ignore) {
        }
        try {
            for (TaskPage taskCanvas : page.getTasks()) {
                resolveStackOverflow(taskCanvas.getTask());
            }
        } catch (NullPointerException ignore) {
        }
        return page;
    }

    public static Property resolveStackOverflow(Property property) {
        property.setPages(null);
        property.setProject(null);
        return property;
    }

    public static Group resolveStackOverflow(Group group) {
        try {
            for (User user : group.getUsers()) {
                resolveStackOverflow(user);
            }
        } catch (NullPointerException ignored) {
        }
        try {

            for (Permission permission : group.getPermission()) {
                resolveStackOverflow(permission.getProject());
            }
        } catch (NullPointerException ignored) {
        }
        try {
            resolveStackOverflow(group.getOwner());
        } catch (NullPointerException ignored) {
        }
        return group;
    }

    public static Task resolveStackOverflow(Task task) {

        try {
            for (TaskValue taskValue : task.getProperties()) {
                if (taskValue.getProperty().getType().equals(TypeOfProperty.USER)) {
                    try {
                        for (User user : (Collection<User>) taskValue.getValue().getValue()) {
                            resolveStackOverflow(user);
                        }
                    } catch (NullPointerException ignored) {
                    }

                }
                resolveStackOverflow(taskValue.getProperty());
            }
        } catch (NullPointerException ignored) {
        }

        try {
            for (Message message : task.getComments()) {
                message.getUser().setPermission(null);
            }
        } catch (NullPointerException ignored) {

        }
        try {
            for (Log log : task.getLogs()) {
                log.getUser().setPermission(null);
            }
        } catch (NullPointerException ignored) {

        }
        return task;

    }

    public static User resolveStackOverflow(User user) {
        try {
            for (Permission permission : user.getPermission()) {
                resolveStackOverflow(permission.getProject());
            }
        } catch (NullPointerException ignored) {
        }
        return user;
    }

    public static Chat resolveStackOverflow(Chat chat) {
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
        return chat;
    }
}
