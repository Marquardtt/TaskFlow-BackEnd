package br.demo.backend.controller.tasks;

import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.service.PropertyValueService;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;
    private PropertyValueService propertyValueService;


    @PostMapping("/{projectId}/{pageId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable String projectId){
        return taskService.save(pageId);
    }

    @PutMapping("/project/{projectId}")
    public TaskGetDTO upDate(@RequestBody Task task){
        return taskService.update(task, false);
    }

    @PatchMapping("/project/{projectId}")
    public TaskGetDTO patch(@RequestBody Task task){
        return taskService.update(task, true);
    }


    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return  propertyValueService.getTasksToday(id);
    }


    @DeleteMapping("/project/{projectId}/{id}")
    public void delete(@PathVariable Long id){
         taskService.delete(id);
    }

    @DeleteMapping("/project/{projectId}/{id}/permanent")
    public void deletePermanent(@PathVariable Long id){
         taskService.deletePermanent(id);
    }

    @PutMapping("/project/{projectId}/redo/{id}")
    public TaskGetDTO redo(@PathVariable Long id){
        return taskService.redo(id);
    }

    //FEITO
    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return  taskService.getDeletedTasks(projectId);
    }
    //Só os donos do projeto podem completar
    @PatchMapping("/{id}/project/{projectId}/complete")
    public TaskGetDTO complete(@PathVariable Long id){
        return  taskService.complete(id);
    }

}
