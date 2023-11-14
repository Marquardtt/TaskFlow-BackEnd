package br.demo.backend.controller;


import br.demo.backend.model.TaskPropertiesId;
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

    @GetMapping("/{id}/{id2}")
    public TaskPropertiesModel findOne(@PathVariable Long id,@PathVariable Long id2){
        return taskPropertiesService.findOne(new TaskPropertiesId(id, id2));
    }

    @GetMapping
    public Collection<TaskPropertiesModel> findAll(){
        return taskPropertiesService.findAll();
    }

    @DeleteMapping("/{id}/{id2}")
    public void delete(@PathVariable Long id,@PathVariable Long id2){
        taskPropertiesService.delete(new TaskPropertiesId(id, id2));
    }

}
