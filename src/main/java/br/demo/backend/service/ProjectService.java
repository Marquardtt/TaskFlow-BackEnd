package br.demo.backend.service;


import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;

    public Collection<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findOne(Long id) {
        return projectRepository.findById(id).get();
    }

    public void update(Project project) {
        projectRepository.save(project);
    }
    public void save(Project project) {
        HashSet<Option> options = new HashSet<>();
        options.add(new Option(null, "To-do", "#FF7A00"));
        options.add(new Option(null, "Doing", "#F7624B"));
        options.add(new Option(null, "Done", "#F04A94"));
        HashSet<Property> properties = new HashSet<>();
        properties.add(new Select(null, "Stats", true, false, options));
        project.setProperties(properties);
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
