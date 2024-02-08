package br.demo.backend.service;


import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private SelectRepository selectRepository;
    private GroupRepository groupRepository;


    public Collection<Project> findAll() {
        Collection<Project> projects = projectRepository.findAll();
        return projects.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public Collection<Project> finAllOfAUser(Long id) {
        Collection<Project> projects = projectRepository.findProjectsByOwner_Id(id);
        Collection<Group> groups = groupRepository.findGroupsByUsersContaining(new User(id));
        projects.addAll(groups.stream().flatMap(g -> g.getPermission().stream().map(Permission::getProject)).toList());
        return projects.stream().distinct().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }
    public Project findOne(Long id) {
        Project project = projectRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(project);
    }

    public void update(Project project) {
        projectRepository.save(project);
    }

    public void save(Project project) {
        Project emptyProject = projectRepository.save(project);
        HashSet<Option> options = new HashSet<>();
        options.add(new Option(null, "To-do", "#FF7A00"));
        options.add(new Option(null, "Doing", "#F7624B"));
        options.add(new Option(null, "Done", "#F04A94"));
        emptyProject.setProperties(new HashSet<>());
        Select select = new Select(null, "Stats", true, false,
                options, TypeOfProperty.SELECT, null, emptyProject);
        Select selectCreated = selectRepository.save(select);
        emptyProject.getProperties().add(selectCreated);
        projectRepository.save(emptyProject);
    }

    public void setVisualizedNow(Project project) {
        project.setVisualizedAt(LocalDateTime.now());
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
