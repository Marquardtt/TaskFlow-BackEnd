package br.demo.backend.controller.pages;

import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.service.pages.CommonPageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/page")
public class CommonPageController {

    private CommonPageService commonPageService;
    @PostMapping
    public void insert(@RequestBody CommonPage page) {
        commonPageService.save(page);
    }

    @PutMapping("/{taskId}/{index}")
    public CommonPage updateIndexes(@RequestBody CommonPage page, @PathVariable Long taskId, @PathVariable Integer index) {
        return commonPageService.updateIndexes(page, taskId, index);
    }

    @PutMapping
    public void upDate(@RequestBody CommonPage page) {
        commonPageService.update(page);
    }

    @GetMapping("/{id}")
    public CommonPage findOne(@PathVariable Long id) {
        return commonPageService.findOne(id);
    }

    @GetMapping
    public Collection<CommonPage> findAll() {
        return commonPageService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commonPageService.delete(id);
    }
}
