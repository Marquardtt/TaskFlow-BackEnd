package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

public class ProjectToGetDTO implements ModelToGetDTO<Project, ProjectGetDTO> {

    private static GroupRepository groupRepository;

    private final MessageToGetDTO messageToGetDTO;
    private final PageToGetDTO pageToGetDTO;
    private final PropertyToGetDTO propertyToGetDTO;

    private final PropertyValueToGetDTO propertyValueToGetDTO;
    private final SimpleGroupToGetDTO simpleGroupToGetDTO;

    public ProjectToGetDTO(MessageToGetDTO messageToGetDTO,
                           PageToGetDTO pageToGetDTO,
                           PropertyToGetDTO propertyToGetDTO,
                           PropertyValueToGetDTO propertyValueToGetDTO,
                           SimpleGroupToGetDTO simpleGroupToGetDTO ) {
        this.messageToGetDTO = messageToGetDTO;
        this.pageToGetDTO = pageToGetDTO;
        this.propertyToGetDTO = propertyToGetDTO;
        this.propertyValueToGetDTO = propertyValueToGetDTO;
        this.simpleGroupToGetDTO = simpleGroupToGetDTO;
    }

    @Override
    public ProjectGetDTO tranform(Project project) {
        if(project == null) return null;
        ProjectGetDTO projectGet = new ProjectGetDTO();
        BeanUtils.copyProperties(project, projectGet);
        try {
            projectGet.setPages(project.getPages().stream().map(pageToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            projectGet.setProperties(project.getProperties().stream().map(propertyToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try{
            projectGet.setComments(project.getComments().stream().map(messageToGetDTO::tranform).toList());
        }catch (NullPointerException ignore){}

        try{
            projectGet.setValues(project.getValues().stream().map(propertyValueToGetDTO::tranform).toList());
        }catch (NullPointerException ignore){}

        projectGet.setOwner(tranformSimple(project.getOwner()));
        return projectGet;
    }

    public SimpleProjectGetDTO tranformSimple(Project project){

        Collection<Group> groups = groupRepository.findGroupsByPermissions_Project(project);

        Integer progress = 0;
        try {
            Collection<Task> tasks = project.getPages().stream()
                    .flatMap(page -> page.getTasks().stream()).map(TaskPage::getTask).toList();
            tasks = tasks.stream().distinct().toList();
            progress = Math.toIntExact(100 / tasks.size() * tasks.stream().filter(Task::getCompleted).count());
        } catch (ArithmeticException | NullPointerException ignore) {
            progress = 100;
        }
        Integer qttyPages = project.getPages() == null ? 0: project.getPages().size();
        Integer qttyProperties = project.getProperties() == null ? 0 : project.getProperties().size();
        OtherUsersDTO user = tranformSimple(project.getOwner());
        return new SimpleProjectGetDTO(
                project.getId(), project.getName(), project.getDescription(), project.getPicture(),
                progress,groups.stream().map(simpleGroupToGetDTO::tranform).toList(),
                user, qttyPages, qttyProperties
        );
    }

    private static OtherUsersDTO tranformSimple(User obj){
        if(obj == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(obj, simpleUser);
        System.out.println(simpleUser);
        simpleUser.setUsername(obj.getUserDetailsEntity().getUsername());
        return simpleUser;
    }
}
