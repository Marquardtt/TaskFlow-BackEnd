package br.demo.backend.controller;


import br.demo.backend.model.ProjectModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ProjectModel findOne(@RequestParam Long id) {
        return projectService.findOne(id);
    }

    @GetMapping
    public List<ProjectModel> findAll() {
        return projectService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        projectService.delete(id);
    }
}
