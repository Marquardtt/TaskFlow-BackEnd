package br.demo.backend.controller.pages;

import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.dtos.relations.TaskCanvasGetDTO;
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
    //FEITO
    @PostMapping
    public PageGetDTO insert(@RequestBody PagePostDTO page) {
        return pageService.save(page);
    }

//    @PatchMapping("/{taskId}/{index}/{columnChanged}")
//    public OrderedPageGetDTO updateIndexes(@RequestBody OrderedPage page, @PathVariable Long taskId,
//                                           @PathVariable Integer index, @PathVariable Integer columnChanged) {
//        return pageService.updateIndex(page, taskId, index, columnChanged);
//    }

    //FEITO
    @PatchMapping("/{id}")
    public PageGetDTO upDateName(@RequestBody(required = false) String name, @PathVariable Long id) {
        return pageService.updateName(name, id);
    }

    //FEITO
    @PatchMapping("/x-and-y")
    public TaskCanvasGetDTO upDate(@RequestBody TaskCanvas taskPage) {
        return pageService.updateXAndY(taskPage);
    }

    //FEITO
    @PatchMapping("/draw/{id}")
    public CanvasPageGetDTO upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id) {
        return pageService.updateDraw(draw, id);
    }

    //FEITO
    @PatchMapping("/prop-ordering/{id}")
    public OrderedPageGetDTO updatePropertiesOrdering(@RequestBody Property property, @PathVariable Long id) {
        return pageService.updatePropertiesOrdering(property, id);
    }

    //General
    //FEITO
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pageService.delete(id);
    }

    //FEITO
    @PatchMapping("/merge/{id}")
    public Collection<PageGetDTO> merge(@RequestBody Collection<Page> pages, @PathVariable Long id) {
        return pageService.merge(pages, id);
    }
}
