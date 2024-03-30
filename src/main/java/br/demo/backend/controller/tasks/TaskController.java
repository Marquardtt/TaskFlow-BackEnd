package br.demo.backend.controller.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;


    //FEITO
    @PostMapping("/{pageId}/{userId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable String userId){
        return taskService.save(pageId, userId);
    }

    //FEITO
    @PutMapping
    public TaskGetDTO upDate(@RequestBody Task task){
        return taskService.update(task, false);
    }
    //FEITO
    @PatchMapping
    public TaskGetDTO patch(@RequestBody Task task){
        return taskService.update(task, true);
    }

    //FEITO
    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return taskService.getTasksToday(id);
    }

    //FEITO
    @DeleteMapping("/{id}/{userId}")
    public void delete(@PathVariable Long id , @PathVariable String userId){
        taskService.delete(id, userId);
    }

    //FEITO
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        taskService.deletePermanent(id);
    }

    //FEITO
    @PutMapping("/redo/{userId}/{id}")
    public TaskGetDTO redo(@PathVariable Long id , @PathVariable String userId){
        return taskService.redo(id, userId);
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
