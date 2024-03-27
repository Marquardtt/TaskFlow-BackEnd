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


    //Precisa ter permissão de post no projeto
    @PostMapping("/{pageId}/{userId}")
    public TaskGetDTO insert(@PathVariable Long pageId, @PathVariable String userId){
        return taskService.save(pageId, userId);
    }

    //Precisa ter permissão de put no projeto
    @PutMapping
    public TaskGetDTO upDate(@RequestBody Task task){
        return taskService.update(task, false);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping
    public TaskGetDTO patch(@RequestBody Task task){
        return taskService.update(task, true);
    }

    //Precisa ser o mesmo usaurio do parametro
    @GetMapping("/today/{id}")
    public Collection<TaskGetDTO> findTodaysTasks(@PathVariable String id){
        return taskService.getTasksToday(id);
    }

    //Precisa ter permissão de delete no projeto
    @DeleteMapping("/{id}/{userId}")
    public void delete(@PathVariable Long id , @PathVariable String userId){
        taskService.delete(id, userId);
    }

    //Precisa ser o owner do projeto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        taskService.deletePermanent(id);
    }

    //Precisa ter permissão de delete no projeto
    @PutMapping("/redo/{userId}/{id}")
    public TaskGetDTO redo(@PathVariable Long id , @PathVariable String userId){
        return taskService.redo(id, userId);
    }

    //Precisa estar no projeto
    @GetMapping("/project/{projectId}")
    public Collection<TaskGetDTO> getDeletedTasks(@PathVariable Long projectId){
        return taskService.getDeletedTasks(projectId);
    }

}
