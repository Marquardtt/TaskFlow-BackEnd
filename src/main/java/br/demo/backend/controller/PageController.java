package br.demo.backend.controller;

import br.demo.backend.model.PageModel;
import br.demo.backend.service.PageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/page")
public class PageController {

    private PageService pageService;
    @PostMapping
    public void insert(@RequestBody PageModel page) {
        pageService.save(page);
    }

    @PutMapping
    public void upDate(@RequestBody PageModel page) {
        pageService.save(page);
    }

    @GetMapping("/{id}")
    public PageModel findOne(@PathVariable Long id) {
        return pageService.findOne(id);
    }

    @GetMapping
    public Collection<PageModel> findAll() {
        return pageService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pageService.delete(id);
    }
}
