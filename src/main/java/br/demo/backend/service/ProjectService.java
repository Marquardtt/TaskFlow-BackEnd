package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ModelToGetDTO;
import br.demo.backend.model.*;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.ProjectPostDTO;
import br.demo.backend.model.dtos.project.ProjectPutDTO;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private SelectRepository selectRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private AutoMapper<Project> autoMapper;



    public Collection<ProjectGetDTO> findAll() {
        return projectRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
    }

    public void updatePicture(MultipartFile picture, Long id) {
        Project project = projectRepository.findById(id).get();
        project.setPicture(new Archive(picture));
        projectRepository.save(project);
    }

    public void updateOwner(User user, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        project.setOwner(user);
        projectRepository.save(project);
    }

    public Collection<ProjectGetDTO> finAllOfAUser(String id) {
        Collection<Project> projects = projectRepository.findProjectsByOwner_UserDetailsEntity_Username(id);
        projects.addAll(userRepository.findByUserDetailsEntity_Username(id)
                .get().getPermissions().stream().map(Permission::getProject).toList());
        return projects.stream().map(ModelToGetDTO::tranform).toList();
    }
    public ProjectGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(projectRepository.findById(id).get());
    }

    public void update(ProjectPutDTO projectDTO, Boolean patching) {
        Project oldProject = projectRepository.findById(projectDTO.getId()).get();
        Project project = patching ? oldProject : new Project();
        User owner = project.getOwner();
        autoMapper.map(projectDTO, project, patching);
        project.setOwner(owner);
        project.setPages(oldProject.getPages());
        project.setProperties(oldProject.getProperties());
        project.setPicture(oldProject.getPicture());
        projectRepository.save(project);
    }

    public void save(ProjectPostDTO projectDto) {
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        project.setOwner(userRepository.findByUserDetailsEntity_Username(projectDto.getOwner().getUserDetailsEntity().getUsername()).get());

        Project emptyProject = projectRepository.save(project);
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(null, "To-do", "#FF7A00", 0));
        options.add(new Option(null, "Doing", "#F7624B", 1));
        options.add(new Option(null, "Done", "#F04A94", 2));
        emptyProject.setProperties(new ArrayList<>());
        Select select = new Select(null, "Stats", true, false,
                options, TypeOfProperty.SELECT, null, emptyProject);
        Select selectCreated = selectRepository.save(select);
        emptyProject.getProperties().add(selectCreated);
        emptyProject.setVisualizedAt(LocalDateTime.now());
        projectRepository.save(emptyProject);
    }

    public void setVisualizedNow(Project projectPut) {
        Project project = projectRepository.findById(projectPut.getId()).get();
        project.setVisualizedAt(LocalDateTime.now());

        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
