package br.demo.backend.controller.pages;

import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.service.pages.PageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/page")
public class PageController {

    private PageService pageService;

    // OrderedPage
    @PostMapping("/project/{projectId}")
    public PageGetDTO insert(@PathVariable Long projectId, @RequestBody PagePostDTO page) {
        return pageService.save(page, projectId);
    }

    @PatchMapping("/{taskId}/{index}/{columnChanged}/project/{projectId}")
    public OrderedPageGetDTO updateIndexes(@PathVariable Long projectId, @RequestBody OrderedPage page, @PathVariable Long taskId,
                                           @PathVariable Integer index, @PathVariable Integer columnChanged) {
        return pageService.updateIndex(page, taskId, index, columnChanged);
    }

    @PatchMapping("/{id}/project/{projectId}")
    public void upDate(@PathVariable Long projectId, @RequestBody(required = false) String name, @PathVariable Long id) {
        pageService.update(name, id);
    }

    @PatchMapping("/{taskId}/{index}/project/{projectId}")
    public PageGetDTO updateIndexes(@PathVariable Long projectId, @RequestBody Page page, @PathVariable Long taskId, @PathVariable Integer index) {
        return pageService.updateIndex(page, taskId, index);
    }

    @PatchMapping("/x-and-y/project/{projectId}")
    public void upDate(@PathVariable Long projectId, @RequestBody TaskCanvas taskPage) {
        pageService.updateXAndY(taskPage);
    }

    @PatchMapping("/draw/{id}/project/{projectId}")
    public void upDateDraw(@PathVariable Long projectId, @RequestParam MultipartFile draw, @PathVariable Long id) {
        pageService.updateDraw(draw, id);
    }

    @PatchMapping("/prop-ordering/{id}/project/{projectId}")
    public void updatePropertiesOrdering(@PathVariable Long projectId, @RequestBody Property property, @PathVariable Long id) {
        pageService.updatePropertiesOrdering(property, id);
    }

    // General
    @GetMapping("/{id}/project/{projectId}")
    public PageGetDTO findOne(@PathVariable Long projectId, @PathVariable Long id) {
        return pageService.findOne(id);
    }

    @GetMapping("/project/{projectId}")
    public Collection<PageGetDTO> findAll(@PathVariable Long projectId) {
        return pageService.findAll();
    }

    @DeleteMapping("/{id}/project/{projectId}")
    public void delete(@PathVariable Long projectId, @PathVariable Long id) {
        pageService.delete(id);
    }

    @PatchMapping("/merge/{id}/project/{projectId}")
    public void merge(@PathVariable Long projectId, @RequestBody Collection<Page> pages, @PathVariable Long id) {
        pageService.merge(pages, id);
    }
}
