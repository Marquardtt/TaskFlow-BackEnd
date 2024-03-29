package br.demo.backend.utils;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.*;
import br.demo.backend.model.dtos.chat.get.*;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.dtos.relations.*;
import br.demo.backend.model.dtos.tasks.LogGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.UserValued;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

public class ModelToGetDTO {

    public static UserGetDTO tranform(User obj){
        if(obj == null) return null;
        UserGetDTO user = new UserGetDTO();
        BeanUtils.copyProperties(obj, user);
        try {
            user.setPermissions(obj.getPermissions().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        return user;
    }
    public static MessageGetDTO tranform(Message obj){
        if(obj == null) return null;
        MessageGetDTO message = new MessageGetDTO();
        BeanUtils.copyProperties(obj, message);
        message.setSender(tranformSimple(obj.getSender()));
        try {
            message.setDestinations(obj.getDestinations().stream().map(ModelToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        return message;
    }

    public static DestinationGetDTO tranform(Destination obj){
        if(obj == null) return null;
        DestinationGetDTO destination = new DestinationGetDTO();
        BeanUtils.copyProperties(obj, destination);
        destination.setUser(tranformSimple(obj.getUser()));
        return destination;
    }
    public static ChatGetDTO tranform(Chat obj){
        if(obj == null) return null;
        ChatGetDTO chat = obj.getType().equals(TypeOfChat.GROUP)  ?
                tranform((ChatGroup) obj): tranform((ChatPrivate) obj);

        try {
            chat.setMessages(obj.getMessages().stream().map(ModelToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        chat.setLastMessage(tranform(obj.getLastMessage()));
        return chat;
    }
    private static ChatPrivateGetDTO tranform(ChatPrivate obj){
        if(obj == null) return null;
        ChatPrivateGetDTO chat = new ChatPrivateGetDTO();
        BeanUtils.copyProperties(obj, chat);
        try {
            chat.setUsers(obj.getUsers().stream().map(ModelToGetDTO::tranformSimple).toList());
        } catch (NullPointerException ignore) {}
        return chat;
    }
    private static ChatGroupGetDTO tranform(ChatGroup obj){
        if(obj == null) return null;
        ChatGroupGetDTO chat = new ChatGroupGetDTO();
        BeanUtils.copyProperties(obj, chat);
        chat.setGroup(tranform(obj.getGroup()));
        return chat;
    }

    public static PermissionGetDTO tranform(Permission obj){
        if(obj == null) return null;
        PermissionGetDTO permission = new PermissionGetDTO();
        BeanUtils.copyProperties(obj, permission);
        permission.setProject(tranform(obj.getProject()));
        return permission;
    }
    public static ProjectGetDTO tranform(Project obj){
        if(obj == null) return null;
        ProjectGetDTO project = new ProjectGetDTO();
        BeanUtils.copyProperties(obj, project);
        try {
            project.setPages(obj.getPages().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            project.setProperties(obj.getProperties().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        project.setOwner(tranformSimple(obj.getOwner()));
        return project;
    }
    public static TaskGetDTO tranform(Task obj){
        if(obj == null) return null;
        TaskGetDTO task = new TaskGetDTO();
        BeanUtils.copyProperties(obj, task);
        try {
            task.setLogs(obj.getLogs().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            task.setComments(obj.getComments().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) { }
        try {
            task.setProperties(obj.getProperties().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        return task;
    }
    public static PageGetDTO tranform(Page obj){
        if(obj == null) return null;
        PageGetDTO page;
        if(obj instanceof OrderedPage){
            page = tranform((OrderedPage) obj);
        }else if(obj instanceof CanvasPage){
            page = new CanvasPageGetDTO();
            BeanUtils.copyProperties(obj, page);
        }else{
            page = new PageGetDTO();
            BeanUtils.copyProperties(obj, page);
        }
        try {
            page.setProperties(obj.getProperties().stream().map(ModelToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            page.setTasks(obj.getTasks().stream().map(ModelToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        return page;
    }
    private static OrderedPageGetDTO tranform(OrderedPage obj){
        if(obj == null) return null;
        OrderedPageGetDTO page = new OrderedPageGetDTO();
        BeanUtils.copyProperties(obj, page);
        page.setPropertyOrdering(tranform(obj.getPropertyOrdering()));
        return page;
    }
    public static TaskPageGetDTO tranform(TaskPage obj){
        if(obj == null) return null;
        TaskPageGetDTO taskPage;
        if(obj instanceof TaskOrdered){
            taskPage = new TaskOrderedGetDTO();
        }else if(obj instanceof TaskCanvas){
            taskPage = new TaskCanvasGetDTO();
        }else {
            taskPage = new TaskPageGetDTO();
        }
        BeanUtils.copyProperties(obj, taskPage);
        taskPage.setTask(tranform(obj.getTask()));
        return taskPage;
    }
    public static PropertyGetDTO tranform(Property obj){
        if(obj == null) return null;
        PropertyGetDTO property;
        if(obj instanceof Date){
            property = new DateGetDTO();
        }else if(obj instanceof Limited){
            property = new LimitedGetDTO();
        }else {
            property = new SelectGetDTO();
        }
        BeanUtils.copyProperties(obj, property);
        return property;
    }
    public static TaskValueGetDTO tranform(TaskValue obj){
        if(obj == null) return null;
        TaskValueGetDTO taskValue = new TaskValueGetDTO();
        BeanUtils.copyProperties(obj, taskValue);
        taskValue.setProperty(tranform(obj.getProperty()));
        if(obj.getValue() instanceof UserValued userValued){
            try{
                UserValuedGetDTO userValuedGet = new UserValuedGetDTO();
                userValuedGet.setValue(((Collection<User>)userValued.getValue())
                        .stream().map(ModelToGetDTO::tranformSimple).toList());
                userValuedGet.setId(userValued.getId());
                taskValue.setValue(userValuedGet);
            }catch (NullPointerException ignore) {}
        }
        return taskValue;
    }

    public static GroupGetDTO tranform(Group obj){
        if(obj == null) return null;
        GroupGetDTO group = new GroupGetDTO();
        BeanUtils.copyProperties(obj, group);
        group.setOwner(tranformSimple(obj.getOwner()));
        try {
            group.setPermissions(obj.getPermissions().stream().map(ModelToGetDTO::tranform).toList());
        }catch (NullPointerException ignore) {}
        try {
            group.setUsers(obj.getUsers().stream().map(ModelToGetDTO::tranformSimple).toList());
        }catch (NullPointerException ignore) {}
        return group;
    }

    public static LogGetDTO tranform(Log obj){
        if(obj == null) return null;
        LogGetDTO log = new LogGetDTO();
        BeanUtils.copyProperties(obj, log);
        log.setUser(tranformSimple(obj.getUser()));
        return log;
    }

    private static SimpleUserGetDTO tranformSimple(User obj){
        if(obj == null) return null;
        SimpleUserGetDTO simpleUser = new SimpleUserGetDTO();
        BeanUtils.copyProperties(obj, simpleUser);
        return simpleUser;
    }

}
