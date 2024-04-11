package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.relations.TaskCanvasGetDTO;
import br.demo.backend.model.dtos.relations.TaskOrderedGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class TaskPageToGetDTO implements ModelToGetDTO<TaskPage, TaskPageGetDTO> {
    private final TaskToGetDTO taskToGetDTO;

    public TaskPageToGetDTO(TaskToGetDTO taskToGetDTO) {
        this.taskToGetDTO = taskToGetDTO;
    }

    @Override
    public TaskPageGetDTO tranform(TaskPage taskPage) {
        if(taskPage == null) return null;
        TaskPageGetDTO taskPageGet;
        if(taskPage instanceof TaskOrdered){
            taskPageGet = new TaskOrderedGetDTO();
        }else if(taskPage instanceof TaskCanvas){
            taskPageGet = new TaskCanvasGetDTO();
        }else {
            taskPageGet = new TaskPageGetDTO();
        }
        BeanUtils.copyProperties(taskPage, taskPageGet);
        taskPageGet.setTask(taskToGetDTO.tranform(taskPage.getTask()));
        return taskPageGet;
    }
}
