package br.demo.backend.controller;


import br.demo.backend.model.TaskPropertiesModel;
import br.demo.backend.service.TaskPropertiesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @GetMapping("/{id}")
    public TaskPropertiesModel findOne(@PathVariable Long id){
        return taskPropertiesService.findOne(id);
    }

    @GetMapping
    public Collection<TaskPropertiesModel> findAll(){
        return taskPropertiesService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        taskPropertiesService.delete(id);
    }

}
