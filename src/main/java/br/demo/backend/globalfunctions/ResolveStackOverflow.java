package br.demo.backend.globalfunctions;
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
        try {
            page.setProject(null);
        } catch (NullPointerException ignore) {
        }
        try {
            ((CommonPage) page).setPropertyOrdering(resolveStackOverflow(((CommonPage) page).getPropertyOrdering()));
        } catch (NullPointerException | ClassCastException ignore) {
        }
        try {
            page.setTasks(page.getTasks().stream().map(t -> {
                t.setTask(resolveStackOverflow(t.getTask()));
                return t;
            }).toList());
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
            group.setUsers(group.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            group.setPermission(group.getPermission().stream().map(p -> {
                p.setProject(resolveStackOverflow(p.getProject()));
                return p;
            }).toList());
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
                    task.getProperties().stream().map(t -> {
                        if (t.getProperty().getType().equals(TypeOfProperty.USER)) {
                            t.getValue().setValue(resolveStackOverflow((User) t.getValue().getValue()));
                        }
                        t.setProperty(resolveStackOverflow(t.getProperty()));
                        return t;
                    }).toList()
            );
        } catch (NullPointerException ignored) {
        }

        try {
            task.setComments(task.getComments().stream().map(m -> {
                m.getUser().setPermission(null);
                return m;
            }).toList());
        } catch (NullPointerException ignored) {

        }
        try {
            task.setLogs(task.getLogs().stream().map(l -> {
                l.getUser().setPermission(null);
                return l;
            }).toList());
        } catch (NullPointerException ignored) {
        }
        return task;

    }

    public static User resolveStackOverflow(User user) {
        try {
            user.setPermission(user.getPermission().stream().map(p -> {
                p.setProject(resolveStackOverflow(p.getProject()));
                return p;
            }).toList());
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
            chat.setMessages(chat.getMessages().stream().map(m -> {
                m.setUser(resolveStackOverflow(m.getUser()));
                return m;
            }).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            resolveStackOverflow(chat.getLastMessage().getUser());
        } catch (NullPointerException ignored) {
        }
        return chat;
    }
}
