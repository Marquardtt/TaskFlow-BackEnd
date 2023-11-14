package br.demo.backend.controller;

import br.demo.backend.model.TaskPageId;
import br.demo.backend.model.TaskPageModel;
import br.demo.backend.service.TaskPageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/task-page")
public class TaskPageController {
    private TaskPageService taskPageService;

    @PostMapping
    public void insert(@RequestBody TaskPageModel task){
        taskPageService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody TaskPageModel task){
        taskPageService.save(task);
    }

    @GetMapping("/{id}/{id2}")
    public TaskPageModel findOne(@PathVariable Long id,@PathVariable Long id2 ){
        return taskPageService.findOne(new TaskPageId(id, id2));
    }

    @GetMapping
    public Collection<TaskPageModel> findAll(){
        return taskPageService.findAll();
    }

    @DeleteMapping("/{id}/{id2}")
    public void delete(@PathVariable Long id,@PathVariable Long id2 ){
        taskPageService.delete(new TaskPageId(id, id2));
    }
}
