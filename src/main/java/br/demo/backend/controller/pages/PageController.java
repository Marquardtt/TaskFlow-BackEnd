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


    //OrderedPage
    @PostMapping
    public PageGetDTO insert(@RequestBody PagePostDTO page) {
        return pageService.save(page);
    }

    //TODO: 11/02/2024 Ver se o indice pode ser atualizado no frontend de forma facil
    @PatchMapping("/{taskId}/{index}/{columnChanged}")
    public OrderedPageGetDTO updateIndexes(@RequestBody OrderedPage page, @PathVariable Long taskId,
                                           @PathVariable Integer index, @PathVariable Integer columnChanged) {
        return pageService.updateIndex(page, taskId, index, columnChanged);
    }

    @PatchMapping("/{id}")
    public void upDate(@RequestBody(required = false) String name, @PathVariable Long id) {
        pageService.update(name, id);
    }


    @PatchMapping("/{taskId}/{index}")
    public PageGetDTO updateIndexes(@RequestBody Page page, @PathVariable Long taskId, @PathVariable Integer index) {
        return pageService.updateIndex(page, taskId, index);
    }


    @PatchMapping("/x-and-y")
    public void upDate(@RequestBody TaskCanvas taskPage) {
        pageService.updateXAndY(taskPage);
    }

    @PatchMapping("/draw/{id}")
    public void upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id) {
        pageService.updateDraw(draw, id);
    }

    @PatchMapping("/prop-ordering/{id}")
    public void updatePropertiesOrdering(@RequestBody Property property, @PathVariable Long id) {
        pageService.updatePropertiesOrdering(property, id);
    }

    //General
    @GetMapping("/{id}")
    public PageGetDTO findOne(@PathVariable Long id) {
        return pageService.findOne(id);
    }

    @GetMapping
    public Collection<PageGetDTO> findAll() {
        return pageService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pageService.delete(id);
    }

    @PatchMapping("/merge/{id}")
    public void merge(@RequestBody Collection<Page> pages, @PathVariable Long id) {
        pageService.merge(pages, id);
    }
}
