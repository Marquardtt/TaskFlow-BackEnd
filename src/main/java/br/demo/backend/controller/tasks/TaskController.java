package br.demo.backend.controller.tasks;

import br.demo.backend.model.tasks.Task;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;

    @PostMapping
    public void insert(@RequestBody Task task){
        taskService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody Task task){
        taskService.update(task);
    }

    @GetMapping("/{id}")
    public Task findOne(@PathVariable Long id){
        return taskService.findOne(id);
    }

    @GetMapping
    public Collection<Task> findAll(){
        return taskService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }
}
