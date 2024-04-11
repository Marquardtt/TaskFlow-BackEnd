package br.demo.backend.service;


import br.demo.backend.model.enums.Action;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.properties.DefaultPropsService;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.utils.Implementacoes;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.ProjectPostDTO;
import br.demo.backend.model.dtos.project.ProjectPutDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private SelectRepository selectRepository;
    private UserRepository userRepository;
    private LogService logService;
    private DefaultPropsService defaultPropsService;
    private AutoMapper<Project> autoMapper;

    public ProjectGetDTO updatePicture(MultipartFile picture, Long id) {
        Project project = projectRepository.findById(id).get();
        project.setPicture(new Archive(picture));
        //generate logs
        logService.updatePicture(project);
        return Implementacoes.tranform(projectRepository.save(project));
    }

    public ProjectGetDTO updateOwner(User user, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        project.setOwner(user);
        //generate logs
        logService.updateOwner(project);
        return Implementacoes.tranform(projectRepository.save(project));
    }

    public Collection<SimpleProjectGetDTO> finAllOfAUser() {
        String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUserDetailsEntity_Username(username).get();
        //get the projects that the user is owner
        Collection<Project> projects = projectRepository.findProjectsByOwner_UserDetailsEntity_Username(username);
        //get the projects that the user is member
        projects.addAll(user.getPermissions().stream().map(Permission::getProject).toList());
        return projects.stream().distinct().map(Implementacoes::tranformSimple).toList();
    }

    public ProjectGetDTO findOne(Long id) {
        return Implementacoes.tranform(projectRepository.findById(id).get());
    }

    public ProjectGetDTO update(ProjectPutDTO projectDTO, Boolean patching) {
        Project oldProject = projectRepository.findById(projectDTO.getId()).get();
        Project project = patching ? oldProject : new Project();
        autoMapper.map(projectDTO, project, patching);
        //keep the owner, pages, properties and picture of the project
        project.setOwner(oldProject.getOwner());
        project.setPages(oldProject.getPages());
        project.setProperties(oldProject.getProperties());
        project.setPicture(oldProject.getPicture());

        //generate the logs
        logService.generateLog(Action.UPDATE, project, oldProject);
        return Implementacoes.tranform(projectRepository.save(project));
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
        emptyProject.setVisualizedAt(LocalDateTime.now());

        return Implementacoes.tranformSimple(projectRepository.save(project));
    }

   //set that the project was visualized by a user, to sort by this on the projects page
    public ProjectGetDTO setVisualizedNow(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        project.setVisualizedAt(LocalDateTime.now());
        return Implementacoes.tranform(projectRepository.save(project));
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
