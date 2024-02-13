package br.demo.backend.service;


import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.*;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private SelectRepository selectRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private AutoMapper<Project> autoMapper;



    public Collection<Project> findAll() {
        Collection<Project> projects = projectRepository.findAll();
        return projects.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
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

    public Collection<Project> finAllOfAUser(Long id) {
        Collection<Project> projects = projectRepository.findProjectsByOwner_Id(id);
        projects.addAll(userRepository.findById(id).get().getPermissions().stream().map(Permission::getProject).toList());
        return projects.stream().distinct().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }
    public Project findOne(Long id) {
        Project project = projectRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(project);
    }

    public void update(Project projectDTO, Boolean patching) {
        Project project = patching ? projectRepository.findById(projectDTO.getId()).get() : new Project();
        User owner = project.getOwner();
        autoMapper.map(projectDTO, project, patching);
        project.setOwner(owner);
        projectRepository.save(project);
    }

    public void save(Project project) {
        Project emptyProject = projectRepository.save(project);
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(null, "To-do", "#FF7A00"));
        options.add(new Option(null, "Doing", "#F7624B"));
        options.add(new Option(null, "Done", "#F04A94"));
        emptyProject.setProperties(new ArrayList<>());
        Select select = new Select(null, "Stats", true, false,
                options, TypeOfProperty.SELECT, null, emptyProject);
        Select selectCreated = selectRepository.save(select);
        emptyProject.getProperties().add(selectCreated);
        projectRepository.save(emptyProject);
    }

    public void setVisualizedNow(Long id) {
        Project project = projectRepository.findById(id).get();
        project.setVisualizedAt(LocalDateTime.now());
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
