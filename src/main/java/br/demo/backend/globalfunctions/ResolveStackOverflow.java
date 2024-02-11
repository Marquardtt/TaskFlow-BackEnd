package br.demo.backend.globalfunctions;
import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.chat.ChatGroup;
import br.demo.backend.model.chat.ChatPrivate;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.chat.ChatPrivateRepository;

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
            ((OrderedPage) page).setPropertyOrdering(resolveStackOverflow(((OrderedPage) page).getPropertyOrdering()));
            ((OrderedPage)page).setTasks(((OrderedPage)page).getTasks().stream().map(t -> {
                t.setTask(resolveStackOverflow(t.getTask()));
                return t;
            }).toList());
        } catch (NullPointerException | ClassCastException ignore) {
        }
        try {
            ((CanvasPage)page).setTasks(((CanvasPage)page).getTasks().stream().map(t -> {
                t.setTask(resolveStackOverflow(t.getTask()));
                return t;
            }).toList());
        } catch (NullPointerException | ClassCastException ignore) {
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
                m.getSender().setPermission(null);
                m.setDestination(m.getDestination().stream().map(d -> {
                    d.getUser().setPermission(null);
                    return d;
                }).toList());
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

    public static ChatGroup resolveStackOverflow(ChatGroup chat) {
        try {
                chat.setGroup(resolveStackOverflow(chat.getGroup()));
        } catch (NullPointerException ignored) {
        }
        return (ChatGroup) resolveMessages(chat);
    }

    private static Chat resolveMessages(Chat chat) {
        try {
            chat.setMessages(chat.getMessages().stream().map(m -> {
                m.setSender(resolveStackOverflow(m.getSender()));
                m.setDestination(m.getDestination().stream().map(d -> {
                    d.setUser(resolveStackOverflow(d.getUser()));
                    return d;
                }).toList());
                return m;
            }).toList());
        } catch (NullPointerException ignored) {
        }
        try {
            chat.getLastMessage().setSender(resolveStackOverflow(chat.getLastMessage().getSender()));
            chat.getLastMessage().setDestination(chat.getLastMessage().getDestination().stream().map(d -> {
                d.setUser(resolveStackOverflow(d.getUser()));
                return d;
            }).toList());
        } catch (NullPointerException ignored) {
        }
        return chat;
    }

    public static ChatPrivate resolveStackOverflow(ChatPrivate chat) {
        try {
                chat.setUsers(chat.getUsers().stream().map(ResolveStackOverflow::resolveStackOverflow).toList());
        } catch (NullPointerException ignored) {
        }

        return (ChatPrivate) resolveMessages(chat);
    }

    public static Chat resolveStackOverflow(Chat chat) {
        if(chat instanceof ChatGroup){
            return resolveStackOverflow((ChatGroup) chat);
        }else{
            return resolveStackOverflow((ChatPrivate) chat);
        }
    }
}
