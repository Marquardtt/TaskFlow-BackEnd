package br.demo.backend.service;


import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    public void save(Project project) {
        projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
