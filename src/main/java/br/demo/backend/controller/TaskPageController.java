package br.demo.backend.controller;

import br.demo.backend.model.TaskPageId;
import br.demo.backend.model.TaskPage;
import br.demo.backend.service.TaskPageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@AllArgsConstructor
@RequestMapping("/task-page")
public class TaskPageController {
    private TaskPageService taskPageService;

    @PostMapping
    public void insert(@RequestBody TaskPage task){
        taskPageService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody TaskPage task){
        taskPageService.save(task);
    }

    @GetMapping("/{taskId}/{propertyId}")
    public TaskPage findOne(@PathVariable Long taskId, @PathVariable Long propertyId ){
        return taskPageService.findOne(new TaskPageId(taskId, propertyId));
    }

    @GetMapping
    public Collection<TaskPage> findAll(){
        return taskPageService.findAll();
    }

    @DeleteMapping("/{taskId}/{propertyId}")
    public void delete(@PathVariable Long taskId,@PathVariable Long propertyId ){
        taskPageService.delete(new TaskPageId(taskId, propertyId));
    }
}
