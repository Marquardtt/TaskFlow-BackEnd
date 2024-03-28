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

    //FEITO
    @PostMapping
    public SimpleProjectGetDTO insert(@RequestBody ProjectPostDTO project) {
        return projectService.save(project);
    }

//   FEITO
    @PatchMapping("/picture/{id}")
    public ProjectGetDTO upDatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        return projectService.updatePicture(picture, id);
    }
//    FEITO
    @PatchMapping("/set-now")
    public ProjectGetDTO setVisualizedNow(@RequestBody Project project) {
        return projectService.setVisualizedNow(project);
    }
    //    FEITO
    @PutMapping
    public ProjectGetDTO upDate(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, false);
    }
    //    FEITO
    @PatchMapping
    public ProjectGetDTO patch(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, true);
    }

    //    FEITO
    @GetMapping("/{id}")
    public ProjectGetDTO findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    //    FEITO
    @GetMapping("/user/{userId}")
    public Collection<SimpleProjectGetDTO> findAllOfAUser(@PathVariable String userId) {
        return projectService.finAllOfAUser(userId);
    }

    //    FEITO
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    //    FEITO
    @PatchMapping("/change-owner/{projectId}")
    public ProjectGetDTO updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        return projectService.updateOwner(newOwner, projectId);
    }
}
