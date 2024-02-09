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
        projectService.update(project, false);
    }
    @PatchMapping
    public void patch(@RequestBody Project project) {
        projectService.update(project, true);
    }

    @GetMapping("/{id}")
    public Project findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    @GetMapping
    public Collection<Project> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/user/{userId}")
    public Collection<Project> findAllOfAUser(@PathVariable Long userId) {
        return projectService.finAllOfAUser(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}
