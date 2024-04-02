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


    @PostMapping("/{projectId}/{pageId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable String projectId){
        return taskService.save(pageId);
    }

    @PutMapping("/project/{projectId}")
    public void upDate(@RequestBody Task task){
        taskService.update(task, false);
    }
    @PatchMapping("/project/{projectId}")
    public void patch(@RequestBody Task task){
        taskService.update(task, true);
    }


    //FEITO
    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return taskService.getTasksToday(id);
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
    public void redo(@PathVariable Long id){
        taskService.redo(id);
    }

    //FEITO
    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return taskService.getDeletedTasks(projectId);
    }
    //Só os donos do projeto podem completar
    //TODO:Depois eu vou mudar para ser o usuario logado do context
    @PatchMapping("/{id}/project/{projectId}/complete")
    public TaskGetDTO complete(@PathVariable Long id){
        return taskService.complete(id);
    }

}
