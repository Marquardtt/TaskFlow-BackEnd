package br.demo.backend.controller.pages;

import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.OtherPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskPage;
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
    @PostMapping("/ordered")
    public void insert(@RequestBody OrderedPage page) {
        pageService.save(page);
    }
    // TODO: 11/02/2024 Ver se o indice pode ser atualizado no frontend de forma facil
    @PatchMapping("/{taskId}/{index}/{columnChanged}")
    public OrderedPage updateIndexes(@RequestBody OrderedPage page, @PathVariable Long taskId, @PathVariable Integer index, @PathVariable Integer columnChanged) {
        return pageService.updateIndexesVerifications(page, taskId, index, columnChanged);
    }

    @PutMapping("/ordered")
    public void upDate(@RequestBody OrderedPage page) {
        pageService.update(page, false);
    }

    @PatchMapping("/ordered")
    public void patch(@RequestBody OrderedPage page) {
        pageService.update(page, true);
    }

    //OtherPage
    @PostMapping("/other")
    public void insert(@RequestBody OtherPage page) {
        pageService.save(page);
    }

    @PatchMapping("/{taskId}/{index}")
    public OtherPage updateIndexes(@RequestBody OtherPage page, @PathVariable Long taskId, @PathVariable Integer index) {
        return pageService.updateIndexesVerifications(page, taskId, index);
    }

    @PutMapping("/other")
    public void upDate(@RequestBody OtherPage page) {
        pageService.update(page, false);
    }

    @PatchMapping("/other")
    public void patch(@RequestBody OtherPage page) {
        pageService.update(page, true);
    }

    //CanvasPage
    @PostMapping("/canvas")
    public void insert(@RequestBody CanvasPage page) {
        pageService.save(page);
    }

    @PutMapping("/canvas")
    public void upDate(@RequestBody CanvasPage page) {
        pageService.update(page, false);
    }

    @PatchMapping("/canvas")
    public void patch(@RequestBody CanvasPage page) {
        pageService.update(page, true);
    }

    @PatchMapping("/XandY")
    public void upDate(@RequestBody TaskCanvas taskPage) {
        pageService.updateXAndY(taskPage);
    }

    @PatchMapping("/draw/{id}")
    public void upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id) {
        pageService.updateDraw(draw, id);
    }

    //General
    @GetMapping("/{id}")
    public Page findOne(@PathVariable Long id) {
        return pageService.findOne(id);
    }

    @GetMapping
    public Collection<? extends Page> findAll() {
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
