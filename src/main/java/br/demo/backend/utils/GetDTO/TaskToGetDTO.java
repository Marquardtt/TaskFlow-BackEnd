package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.utils.TransformSimple;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class TaskToGetDTO implements ModelToGetDTO<Task, TaskGetDTO> {

    private final MessageToGetDTO messageToGetDTO;

    public TaskToGetDTO(MessageToGetDTO messageToGetDTO) {
        this.messageToGetDTO = messageToGetDTO;
    }

    @Override
    public TaskGetDTO tranform(Task task) {
        if(task == null) return null;
        TaskGetDTO taskGet = new TaskGetDTO();
        BeanUtils.copyProperties(task, taskGet);
        try {
            taskGet.setLogs(task.getLogs().stream().map(TransformSimple::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            taskGet.setComments(task.getComments().stream().map(messageToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) { }
        try {
            taskGet.setProperties(task.getProperties().stream().map(TransformSimple::tranform).toList());
        } catch (NullPointerException ignore) {}
        return taskGet;
    }
}
