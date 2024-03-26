package br.demo.backend.controller.pages;

import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
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
    //Precisa ter permissão de post no projeto
    @PostMapping
    public PageGetDTO insert(@RequestBody PagePostDTO page) {
        return pageService.save(page);
    }

//    @PatchMapping("/{taskId}/{index}/{columnChanged}")
//    public OrderedPageGetDTO updateIndexes(@RequestBody OrderedPage page, @PathVariable Long taskId,
//                                           @PathVariable Integer index, @PathVariable Integer columnChanged) {
//        return pageService.updateIndex(page, taskId, index, columnChanged);
//    }

    //Precisa ter permissão de put no projeto
    @PatchMapping("/{id}")
    public void upDateName(@RequestBody(required = false) String name, @PathVariable Long id) {
        pageService.updateName(name, id);
    }

    //Precisa ter permissão de put no projeto
    @PatchMapping("/x-and-y")
    public void upDate(@RequestBody TaskCanvas taskPage) {
        pageService.updateXAndY(taskPage);
    }

    //Precisa ter permissão de put no projeto
    @PatchMapping("/draw/{id}")
    public void upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id) {
        pageService.updateDraw(draw, id);
    }

    //Precisa ter permissão de put no projeto
    @PatchMapping("/prop-ordering/{id}")
    public void updatePropertiesOrdering(@RequestBody Property property, @PathVariable Long id) {
        pageService.updatePropertiesOrdering(property, id);
    }

    //General
    //Precisa ter permissão de delete no projeto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pageService.delete(id);
    }

    //Precisa ter permissão de put no projeto
    @PatchMapping("/merge/{id}")
    public void merge(@RequestBody Collection<Page> pages, @PathVariable Long id) {
        pageService.merge(pages, id);
    }
}
