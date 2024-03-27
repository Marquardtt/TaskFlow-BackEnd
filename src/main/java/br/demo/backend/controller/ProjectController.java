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

    //Precisa estar logado
    @PostMapping
    public ProjectGetDTO insert(@RequestBody ProjectPostDTO project) {
        return projectService.save(project);
    }

//    precisa ser o owner do projeto
    @PatchMapping("/picture/{id}")
    public ProjectGetDTO upDatePicture(@RequestParam MultipartFile picture, @PathVariable Long id) {
        return projectService.updatePicture(picture, id);
    }
//    precisa ser o owner ou o membro do projeto
    @PatchMapping("/set-now")
    public ProjectGetDTO setVisualizedNow(@RequestBody Project project) {
        return projectService.setVisualizedNow(project);
    }
    //    precisa ser o owner do projeto
    @PutMapping
    public ProjectGetDTO upDate(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, false);
    }
    //    precisa ser o owner do projeto
    @PatchMapping
    public ProjectGetDTO patch(@RequestBody ProjectPutDTO project) {
        return projectService.update(project, true);
    }

    //    precisa ser o owner do projeto ou um membro
    @GetMapping("/{id}")
    public ProjectGetDTO findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    //    precisa estar logado com o mesmo user da requisição
    @GetMapping("/user/{userId}")
    public Collection<SimpleProjectGetDTO> findAllOfAUser(@PathVariable String userId) {
        return projectService.finAllOfAUser(userId);
    }

    //    precisa ser o owner do projeto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    //    precisa ser o owner do projeto
    @PatchMapping("/change-owner/{projectId}")
    public ProjectGetDTO updateOwner(@RequestBody User newOwner, @PathVariable Long projectId) {
        return projectService.updateOwner(newOwner, projectId);
    }
}
