package br.demo.backend.controller;

import br.demo.backend.model.TaskPageModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public TaskPageModel findOne(@RequestParam Long id){
        return taskPageService.findOne(id);
    }

    @GetMapping
    public List<TaskPageModel> findAll(){
        return taskPageService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        taskPageService.delete(id);
    }
}
