package br.demo.backend.service;


import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.service.pages.CanvasService;
import br.demo.backend.service.pages.CommonPageService;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private SelectRepository selectRepository;
    private CanvasService canvasService;
    private CommonPageService commonPageService;

    public void setProjectInPropertyOfProjectNull(Project project) {
        for (Property p : project.getProperties()) {
            p.setProject(null);
            p.setPages(null);
        }
        for (Page page : project.getPages()) {
            if (page.getType().equals(TypeOfPage.CANVAS)) {
                canvasService.resolveStackOverflow((Canvas) page);
            } else {
                commonPageService.resolveStackOverflow((CommonPage) page);
            }
        }
        try {
            project.getOwner().setProjects(null);
        } catch (NullPointerException ignored) {
        }
    }

    public Collection<Project> findAll() {
        Collection<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            setProjectInPropertyOfProjectNull(project);
        }
        return projects;
    }

    public Project findOne(Long id) {
        Project project = projectRepository.findById(id).get();
        setProjectInPropertyOfProjectNull(project);
        return project;
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
