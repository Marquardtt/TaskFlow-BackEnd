package br.demo.backend.controller;

import br.demo.backend.model.TaskModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;

    @PostMapping
    public void insert(@RequestBody TaskModel task){
        taskService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody TaskModel task){
        taskService.save(task);
    }

    @GetMapping("/{id}")
    public TaskModel findOne(@RequestParam Long id){
        return taskService.findOne(id);
    }

    @GetMapping
    public List<TaskModel> findAll(){
        return taskService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        taskService.delete(id);
    }
}
