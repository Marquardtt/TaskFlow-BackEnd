package br.demo.backend.utils;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.*;
import br.demo.backend.model.dtos.chat.get.*;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.dtos.relations.*;
import br.demo.backend.model.dtos.tasks.LogGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.enums.TypeOfChat;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.UserValued;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.utils.GetDTO.PermissionToGetDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;


@Component
public class TransformSimple {

    private static GroupRepository groupRepository;

    @Autowired
    public void setUserRepository(GroupRepository repo) {
        TransformSimple.groupRepository= repo;
    }


    private static OtherUsersDTO tranformSimple(User obj){
        if(obj == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(obj, simpleUser);
        System.out.println(simpleUser);
        simpleUser.setUsername(obj.getUserDetailsEntity().getUsername());
        return simpleUser;
    }

    public static SimpleProjectGetDTO tranformSimple(Project obj){

        Collection <Group> groups = groupRepository.findGroupsByPermissions_Project(obj);

        Integer progress = 0;
        try {
            Collection<Task> tasks = obj.getPages().stream()
                    .flatMap(page -> page.getTasks().stream()).map(TaskPage::getTask).toList();
            tasks = tasks.stream().distinct().toList();
            progress = Math.toIntExact(100 / tasks.size() * tasks.stream().filter(Task::getCompleted).count());
        } catch (ArithmeticException | NullPointerException ignore) {
            progress = 100;
        }
        Integer qttyPages = obj.getPages() == null ? 0: obj.getPages().size();
        Integer qttyProperties = obj.getProperties() == null ? 0 : obj.getProperties().size();
        OtherUsersDTO user = tranformSimple(obj.getOwner());
        return new SimpleProjectGetDTO(
                obj.getId(), obj.getName(), obj.getDescription(), obj.getPicture(),
                progress,groups.stream().map(TransformSimple::tranformSimple).toList(),
                user, qttyPages, qttyProperties
        );

    }

    public static SimpleGroupGetDTO tranformSimple(Group obj){
        if(obj == null) return null;
        SimpleGroupGetDTO group = new SimpleGroupGetDTO();
        BeanUtils.copyProperties(obj, group);
        return group;
    }

    public static OtherUsersDTO transformOther(User user){
        OtherUsersDTO other = new OtherUsersDTO();
        BeanUtils.copyProperties(user, other);
        other.setUsername(user.getUserDetailsEntity().getUsername());
        other.setPermissions(user.getPermissions().stream().map(TransformSimple::tranform).toList());
        return other;
    }

}
