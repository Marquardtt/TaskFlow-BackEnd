package br.demo.backend.controller.pages;

import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.dtos.relations.TaskCanvasGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.service.pages.PageService;
import br.demo.backend.utils.IdProjectValidation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/page")
public class PageController {

    private PageService pageService;
    private IdProjectValidation validation;

    // OrderedPage
    @PostMapping("/project/{projectId}")
    public PageGetDTO insert(@PathVariable Long projectId, @RequestBody PagePostDTO page) {
        validation.ofObject(projectId, page.getProject());
        return pageService.save(page, projectId);
    }
    @PatchMapping("/{id}/project/{projectId}")
    public PageGetDTO upDate(@PathVariable Long projectId, @RequestBody(required = false) String name, @PathVariable Long id) {
        return pageService.updateName(name, id, projectId);
    }
    @PatchMapping("/task-page/project/{projectId}")
    public TaskPageGetDTO upDateTaskPage(@RequestBody TaskPage taskPage, @PathVariable Long projectId) {
        return pageService.updateTaskPage(taskPage, projectId);
    }

    @PatchMapping("/draw/{id}/project/{projectId}")
    public PageGetDTO upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id, @PathVariable Long projectId) {
        return pageService.updateDraw(draw, id, projectId);
    }

    @PatchMapping("/prop-ordering/{id}/project/{projectId}")
    public PageGetDTO updatePropertiesOrdering(@PathVariable Long projectId, @RequestBody Property property, @PathVariable Long id) {
        return pageService.updatePropertiesOrdering(property, id, projectId);
    }

    @DeleteMapping("/{id}/project/{projectId}")
    public void delete( @PathVariable Long id, @PathVariable Long projectId) {
         pageService.delete(id, projectId);
    }

    @PatchMapping("/merge/{id}/project/{projectId}")
    public Collection<PageGetDTO> merge( @RequestBody Collection<Page> pages, @PathVariable Long id, @PathVariable Long projectId) {
        return pageService.merge(pages, id, projectId);
    }
}
