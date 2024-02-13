package br.demo.backend.controller;


import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("/picture/{id}")
    public void upDatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        projectService.updatePicture(picture, id);
    }
    @PatchMapping("/set-now/{id}")
    public void setVisualizedNow(@PathVariable Long id) {
        projectService.setVisualizedNow(id);
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

    @PatchMapping("/change-owner/{projectId}")
    public void updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        projectService.updateOwner(newOwner, projectId);
    }
}
