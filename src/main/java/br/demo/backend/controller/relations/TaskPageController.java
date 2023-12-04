package br.demo.backend.controller.relations;

import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.service.relations.TaskCanvasService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@AllArgsConstructor
@RequestMapping("/task-page")
public class TaskPageController {
    private TaskCanvasService taskPageService;

    @PostMapping
    public void insert(@RequestBody TaskCanvas task){
        taskPageService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody TaskCanvas task){
        taskPageService.save(task);
    }

    @GetMapping("/{id}")
    public TaskCanvas findOne(@PathVariable Long id){
        return taskPageService.findOne(id);
    }

    @GetMapping
    public Collection<TaskCanvas> findAll(){
        return taskPageService.findAll();
    }

    @DeleteMapping("/id}")
    public void delete(@PathVariable Long id ){
        taskPageService.delete(id);
    }
}
