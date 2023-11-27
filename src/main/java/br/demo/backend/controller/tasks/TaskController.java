package br.demo.backend.controller.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.tasks.TaskPostDTO;
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
    public void insert(@RequestBody Page page, @RequestBody User user){
        taskService.save(page, user);
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id , @RequestBody User user){
        taskService.delete(id, user);
    }

    @PutMapping("/redo")
    public void redo(@PathVariable Long id , @RequestBody User user){
        taskService.redo(id, user);
    }
}
