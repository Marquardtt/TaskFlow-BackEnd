package br.demo.backend.service;


import br.demo.backend.model.ProjectModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;

    public Collection<ProjectModel> findAll() {
        return projectRepository.findAll();
    }

    public ProjectModel findOne(Long id) {
        return projectRepository.findById(id).get();
    }

    public void save(ProjectModel projectModel) {
        projectRepository.save(projectModel);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
