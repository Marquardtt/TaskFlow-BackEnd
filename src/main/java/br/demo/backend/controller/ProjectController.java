package br.demo.backend.controller;


import br.demo.backend.model.ProjectModel;
import br.demo.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;
    @PostMapping
    public void insert(@RequestBody ProjectModel project) {
        projectService.save(project);
    }

    @PutMapping
    public void upDate(@RequestBody ProjectModel project) {
        projectService.save(project);
    }

    @GetMapping("/{id}")
    public ProjectModel findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    @GetMapping
    public Collection<ProjectModel> findAll() {
        return projectService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}
