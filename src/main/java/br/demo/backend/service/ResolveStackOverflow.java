package br.demo.backend.service;
import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;

public class ResolveStackOverflow {

    public static Project resolveStackOverflow(Project project) {
        try {
            project.setPages(project.getPages().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            project.setProperties(project.getProperties().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }
        project.setOwner(resolveStackOverflow(project.getOwner()));
        return project;
    }

    public static Page resolveStackOverflow(Page page) {
        try {
            page.setProperties(page.getProperties().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignore) {
        }
        page.getProject().setPages(null);
        page.getProject().setProperties(null);
        page.getProject().setOwner(null);
        try {
            ((CommonPage) page).setPropertyOrdering(resolveStackOverflow(((CommonPage) page).getPropertyOrdering()));
        } catch (NullPointerException | ClassCastException ignore) {
        }
        try {
            page.setTasks(page.getTasks().stream().peek(t -> t.setTask(resolveStackOverflow(t.getTask()))).toList());
        } catch (NullPointerException ignore) {
        }
        return page;
    }

    public static Property resolveStackOverflow(Property property) {
        property.setPages(property.getPages().stream().peek(p -> {
            p.setTasks(null);
            p.setProperties(null);
            p.setProject(null);
        }).toList());
        property.getProject().setOwner(null);
        property.getProject().setProperties(null);
        property.getProject().setPages(null);
        return property;
    }

    public static Group resolveStackOverflow(Group group) {
        try {
            group.setUsers(group.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            group.setPermission(group.getPermission().stream().peek(p -> p.setProject(resolveStackOverflow(p.getProject()))).toList());
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
            task.setProperties(
                    task.getProperties().stream().peek(t -> {
                        if (t.getProperty().getType().equals(TypeOfProperty.USER)) {
                            t.getValue().setValue(resolveStackOverflow((User) t.getValue().getValue()));
                        }
                        t.setProperty(resolveStackOverflow(t.getProperty()));
                    }).toList()
            );
        } catch (NullPointerException ignored) {
        }

        try {
            task.setComments(task.getComments().stream().peek(m -> m.getUser().setPermission(null)).toList());
        } catch (NullPointerException ignored) {

        }
        try {
            task.setLogs(task.getLogs().stream().peek(l -> l.getUser().setPermission(null)).toList());
        } catch (NullPointerException ignored) {
        }
        return task;

    }

    public static User resolveStackOverflow(User user) {
        try {
            user.setPermission(user.getPermission().stream().peek(p -> p.setProject(resolveStackOverflow(p.getProject()))).toList());
        } catch (NullPointerException ignored) {
        }
        return user;
    }

    public static Chat resolveStackOverflow(Chat chat) {
        try {
            chat.setUsers(chat.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            chat.setMessages(chat.getMessages().stream().peek(m -> m.setUser(resolveStackOverflow(m.getUser()))).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            resolveStackOverflow(chat.getLastMessage().getUser());
        } catch (NullPointerException ignored) {
        }
        return chat;
    }
}
