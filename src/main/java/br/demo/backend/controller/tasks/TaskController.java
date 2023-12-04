package br.demo.backend.controller.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.pages.Page;
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

    @PostMapping("/{pageId}/{userId}")
    public void insert(@PathVariable Long pageId, @PathVariable Long userId){
        taskService.save(pageId, userId);
    }

    @PutMapping
    public void upDate(@RequestBody Task task){
        taskService.update(task);
    }

    @GetMapping("/{id}")
    public Task findOne(@PathVariable Long id){
        return taskService.findOne(id);
    }

    @GetMapping("/name/{name}")
    public Collection<Task> findByName(@PathVariable String name){
        return taskService.findByName(name);
    }

    @GetMapping
    public Collection<Task> findAll(){
        return taskService.findAll();
    }

    @GetMapping("/today/{id}")
    public Collection<Task> findTodaysTasks(@PathVariable Long id){
        return taskService.getTasksToday(id);
    }

    @DeleteMapping("/{id}/{userId}")
    public void delete(@PathVariable Long id , @PathVariable Long userId){
        taskService.delete(id, userId);
    }

    @PutMapping("/redo/{userId}")
    public void redo(@PathVariable Long id , @PathVariable Long userId){
        taskService.redo(id, userId);
    }

    @GetMapping("/month/{month}/{pageId}/{propertyId}")
    public Collection<Task> getTasksOfMonth(@PathVariable Integer month,
                                            @PathVariable Long pageId,
                                            @PathVariable Long propertyId){
        return taskService.getTasksOfMonth(month, pageId, propertyId);
    }

}
