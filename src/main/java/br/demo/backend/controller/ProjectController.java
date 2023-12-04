package br.demo.backend.controller;


import br.demo.backend.model.Project;
import br.demo.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;
    @PostMapping
    public void insert(@RequestBody Project project) {
        projectService.save(project);
    }


    @PutMapping("/set-now")
    public void setVisualizedNow(@RequestBody Project project) {
        projectService.setVisualizedNow(project);
    }
    @PutMapping
    public void upDate(@RequestBody Project project) {
        projectService.update(project);
    }

    @GetMapping("/{id}")
    public Project findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    @GetMapping
    public Collection<Project> findAll() {
        return projectService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}
