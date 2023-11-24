package br.demo.backend.controller.pages;

import br.demo.backend.model.pages.Page;
import br.demo.backend.service.pages.PageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/page")
public class PageController {

    private PageService pageService;
    @PostMapping
    public void insert(@RequestBody Page page) {
        pageService.save(page);
    }

    @PutMapping
    public void upDate(@RequestBody Page page) {
        pageService.save(page);
    }

    @GetMapping("/{id}")
    public Page findOne(@PathVariable Long id) {
        return pageService.findOne(id);
    }

    @GetMapping
    public Collection<Page> findAll() {
        return pageService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pageService.delete(id);
    }
}
