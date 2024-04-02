package br.demo.backend.controller.tasks;

import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
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


    @PostMapping("/{projectId}/{pageId}/{userId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable String userId, @PathVariable String projectId){
        return taskService.save(pageId, userId);
    }

    @PutMapping("/project/{projectId}")
    public void upDate(@RequestBody Task task){
        taskService.update(task, false);
    }
    @PatchMapping("/project/{projectId}")
    public void patch(@RequestBody Task task){
        taskService.update(task, true);
    }

    @GetMapping("/project/{projectId}/{id}")
    public TaskGetDTO findOne(@PathVariable Long id){
        return taskService.findOne(id);
    }

    @GetMapping("/project/{projectId}/name/{name}")
    public Collection<TaskGetDTO> findByName(@PathVariable String name){
        return taskService.findByName(name);
    }

    @GetMapping("/project/{projectId}/findAll")
    public Collection<TaskGetDTO> findAll(){
        return taskService.findAll();
    }

    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return taskService.getTasksToday(id);
    }

    @DeleteMapping("/{id}/{userId}")
    public void delete(@PathVariable Long id , @PathVariable String userId){
        taskService.delete(id, userId);
    }

    @DeleteMapping("/project/{projectId}/{id}")
    public void delete(@PathVariable Long id){
        taskService.deletePermanent(id);
    }

    @PutMapping("/project/{projectId}/redo/{userId}/{id}")
    public void redo(@PathVariable Long id , @PathVariable String userId){
        taskService.redo(id, userId);
    }

    @GetMapping("/month/{month}/{pageId}/{propertyId}")
    public Collection<TaskPageGetDTO> getTasksOfMonth(@PathVariable Integer month,
                                                      @PathVariable Long pageId,
                                                      @PathVariable Long propertyId){
        return taskService.getTasksOfMonth(month, pageId, propertyId);
    }



    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return taskService.getDeletedTasks(projectId);
    }

}
