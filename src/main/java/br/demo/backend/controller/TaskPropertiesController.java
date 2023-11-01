package br.demo.backend.controller;


import br.demo.backend.model.TaskPropertiesModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/task-properties")
public class TaskPropertiesController {
    
    private TaskPropertiesService taskPropertiesService;
    @PostMapping
    public void insert(@RequestBody TaskPropertiesModel task){
        taskPropertiesService.save(task);
    }

    @PutMapping
    public void upDate(@RequestBody TaskPropertiesModel task){
        taskPropertiesService.save(task);
    }

    @GetMapping
    public TaskPropertiesModel findOne(@RequestParam Long id){
        return taskPropertiesService.findOne(id);
    }

    @GetMapping
    public List<TaskPropertiesModel> findAll(){
        return taskPropertiesService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        taskPropertiesService.delete(id);
    }

}
