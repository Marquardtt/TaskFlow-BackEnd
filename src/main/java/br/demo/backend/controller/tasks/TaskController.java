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


    //FEITO
    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return taskService.getTasksToday(id);
    }

    //TODO: verificar se o becker fez isso
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

    //FEITO
    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return taskService.getDeletedTasks(projectId);
    }
    //Só os donos do projeto podem completar
    //TODO:Depois eu vou mudar para ser o usuario logado do context
    @PatchMapping("/{id}/complete/user/{username}")
    public TaskGetDTO complete(@PathVariable Long id,@PathVariable String username){
        return taskService.complete(id, username);
    }

}
