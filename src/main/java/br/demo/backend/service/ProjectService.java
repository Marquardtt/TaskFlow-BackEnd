package br.demo.backend.service;


import br.demo.backend.exception.SomeUserAlreadyIsInProjectException;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.enums.TypeOfNotification;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.repository.PermissionRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.properties.DefaultPropsService;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.ProjectPostDTO;
import br.demo.backend.model.dtos.project.ProjectPutDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.utils.ResizeImage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private LogService logService;
    private GroupRepository groupRepository;
    private DefaultPropsService defaultPropsService;
    private AutoMapper<Project> autoMapper;
    private PermissionRepository permissionRepositoru;
    private NotificationService notificationService;
    private PropertyValueService propertyValueService;

    public ProjectGetDTO updatePicture(MultipartFile picture, Long id) {
        Project project = projectRepository.findById(id).get();
        project.setPicture(new Archive(picture));
        //generate logs
        try {
            project.getPicture().setData(ResizeImage.resizeImage(picture, 100, 100));
        } catch (IOException ignore) {
        }
        logService.updatePicture(project);
        return ModelToGetDTO.tranform(projectRepository.save(project));
    }

    public ProjectGetDTO updateOwner(OtherUsersDTO userDto, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        User user = userRepository.findById(userDto.getId()).get();
        Permission defaultPermission = permissionRepositoru.findByProjectAndIsDefault(project, true);
        project.getOwner().getPermissions().add(defaultPermission);
        userRepository.save(project.getOwner());
        project.setOwner(user);
        //generate logs
        logService.updateOwner(project);
        return ModelToGetDTO.tranform(projectRepository.save(project));
    }

    @Transactional
    public Collection<SimpleProjectGetDTO> finAllOfAUser() {
        String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        return findProjectsByUser(user).stream().map(ModelToGetDTO::tranformSimple).toList();
    }

    @Transactional
    public Collection<Project> findProjectsByUser(User user){
        Collection<Project> projects = projectRepository.findProjectsByOwner_UserDetailsEntity_Username(user.getUserDetailsEntity().getUsername());
        //get the projects that the user is member
        projects.addAll(user.getPermissions().stream().map(Permission::getProject).toList());
        //get the projects that the user is owner of some group
        Collection<Group> groups = groupRepository.findGroupsByOwner_UserDetailsEntity_Username(user.getUserDetailsEntity().getUsername());
        groups.forEach(group -> projects.addAll(group.getPermissions().stream().map(Permission::getProject).toList()));
        return projects.stream().distinct().toList();
    }

    public ProjectGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(projectRepository.findById(id).get());
    }

    public ProjectGetDTO update(ProjectPutDTO projectDTO, Boolean patching) {
        Project oldProject = projectRepository.findById(projectDTO.getId()).get();
        Project project = new Project();
        if(patching) BeanUtils.copyProperties(oldProject, project);
        autoMapper.map(projectDTO, project, patching);
        //keep the owner, pages, properties and picture of the project
        keepFileds(project, oldProject);
        logService.generateLog(Action.UPDATE,project,oldProject);
        if(changeDescription(oldProject, project)){
            logService.updateDescription(project);
        }
        //generate the logs
        return ModelToGetDTO.tranform(projectRepository.save(project));
    }

    private  void keepFileds(Project project, Project oldProject) {
        project.setOwner(oldProject.getOwner());
        project.setPages(oldProject.getPages());
        project.setProperties(oldProject.getProperties());
        project.setPicture(oldProject.getPicture());
        project.setLogs(oldProject.getLogs());
        project.setValues(propertyValueService.createNotSaved(project));
        Stream<PropertyValue> archiveProps = project.getValues().stream().filter(p -> p.getProperty().getType().equals(TypeOfProperty.ARCHIVE));
        Stream<PropertyValue> archivePropsOld = archiveProps.map(p -> oldProject.getValues().stream().filter(o -> o.equals(p)).findFirst().orElse(p));
        List<PropertyValue> finalProps = archivePropsOld.toList();
        ArrayList<PropertyValue> projectProps = new ArrayList<>(project.getValues());
        projectProps.removeAll(finalProps.stream().filter(p -> project.getValues().contains(p)).toList());
        projectProps.addAll(finalProps);
        project.setValues(projectProps);
    }

    private Boolean changeDescription(Project oldProject, Project project){
        return oldProject.getDescription() == null && project.getDescription() != null ||
                project.getDescription() == null && oldProject.getDescription() != null ||
                oldProject.getDescription() != null &&
                        !oldProject.getDescription().equals(project.getDescription());
    }

    public SimpleProjectGetDTO save(ProjectPostDTO projectDto) {

        String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        //create the project and set the owner
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        project.setOwner(userRepository.findByUserDetailsEntity_Username(username).get());
        Project emptyProject = projectRepository.save(project);

        //generate logs
        logService.generateLog(Action.CREATE, project);

        //set a default property
        defaultPropsService.select(project, null);
        emptyProject.setVisualizedAt(OffsetDateTime.now());

        return ModelToGetDTO.tranformSimple(projectRepository.save(project));
    }

   //set that the project was visualized by a user, to sort by this on the projects page
    public ProjectGetDTO setVisualizedNow(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        project.setVisualizedAt(OffsetDateTime.now());
        return ModelToGetDTO.tranform(projectRepository.save(project));
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public void inviteAGroup(Long projectId, SimpleGroupGetDTO groupdto) {
        Group group = groupRepository.findById(groupdto.getId()).get();
        for(User user : group.getUsers()){
            if(user.getPermissions().stream().anyMatch(p -> p.getProject().getId().equals(projectId))){
                throw new SomeUserAlreadyIsInProjectException();
            }else if(projectRepository.findById(projectId).get().getOwner().equals(user)){
                throw new SomeUserAlreadyIsInProjectException();
            }
        }
        notificationService.generateNotification(TypeOfNotification.INVITETOPROJECT, projectId, group.getId());
    }
}
