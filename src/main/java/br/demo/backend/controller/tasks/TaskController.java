package br.demo.backend.controller.tasks;

import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.service.PropertyValueService;
import br.demo.backend.service.tasks.TaskService;
import br.demo.backend.utils.IdGroupValidation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;
    private PropertyValueService propertyValueService;
    private IdGroupValidation validation;

    @PostMapping("/{projectId}/{pageId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable Long projectId){
        return taskService.save(pageId, projectId);
    }

    @PutMapping("/project/{projectId}")
    public TaskGetDTO upDate(@RequestBody Task task, @PathVariable Long projectId){

        return taskService.update(task, false, projectId);
    }

    @PatchMapping("/project/{projectId}")
    public TaskGetDTO patch(@RequestBody Task task, @PathVariable Long projectId){
        return taskService.update(task, true, projectId);
    }


    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return  propertyValueService.getTasksToday(id);
    }


    @DeleteMapping("/project/{projectId}/{id}")
    public void delete(@PathVariable Long id, @PathVariable Long projectId){
         taskService.delete(id, projectId);
    }

    @DeleteMapping("/project/{projectId}/{id}/permanent")
    public void deletePermanent(@PathVariable Long id, @PathVariable Long projectId){
         taskService.deletePermanent(id, projectId);
    }

    @PutMapping("/project/{projectId}/redo/{id}")
    public TaskGetDTO redo(@PathVariable Long id, @PathVariable Long projectId){
        return taskService.redo(id, projectId);
    }

    //FEITO
    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return  taskService.getDeletedTasks(projectId);
    }
    //Só os donos do projeto podem completar
    @PatchMapping("/{id}/project/{projectId}/complete")
    public TaskGetDTO complete(@PathVariable Long id, @PathVariable Long projectId){
        return  taskService.complete(id, projectId);
    }
    @PatchMapping("/{id}/project/{projectId}/complete-deny")
    public TaskGetDTO completeDeny(@PathVariable Long id, @PathVariable Long projectId){
        return  taskService.cancelComplete(id, projectId);
    }


}
