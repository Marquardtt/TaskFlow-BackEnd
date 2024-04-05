package br.demo.backend.controller;


import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.project.ProjectGetDTO;
import br.demo.backend.model.dtos.project.ProjectPostDTO;
import br.demo.backend.model.dtos.project.ProjectPutDTO;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
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
    public SimpleProjectGetDTO insert(@RequestBody ProjectPostDTO project) {
        return projectService.save(project);
    }

    @PatchMapping("/{projectId}/picture")
    public ProjectGetDTO upDatePicture(@RequestParam MultipartFile picture, @PathVariable("projectId") Long id) {
        return projectService.updatePicture(picture, id);
    }
    @PatchMapping("/{projectId}/set-now")
    public ProjectGetDTO setVisualizedNow(@PathVariable Long projectId){
        return projectService.setVisualizedNow(projectId);
    }
    @PutMapping("/{projectId}")
    public ProjectGetDTO upDate(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, false);
    }
    @PatchMapping("/{projectId}")
    public ProjectGetDTO patch(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, true);
    }

    @GetMapping("/{projectId}")
    public ProjectGetDTO findOne(@PathVariable("projectId") Long id) {
        return projectService.findOne(id);
    }

    @GetMapping("/my")
    public Collection<SimpleProjectGetDTO> findAllOfAUser() {
        return projectService.finAllOfAUser();
    }

    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable("projectId") Long id) {
         projectService.delete(id);
    }

    //    FEITO
    @PatchMapping("/{projectId}/change-owner")
    public ProjectGetDTO updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        return projectService.updateOwner(newOwner, projectId);
    }
}
