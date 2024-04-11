package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.tasks.TaskGetDTO;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class TaskToGetDTO implements ModelToGetDTO<Task, TaskGetDTO> {

    private final MessageToGetDTO messageToGetDTO;

    private final LogToGetDTO logToGetDTO;

    private final PropertyValueToGetDTO propertyValueToGetDTO;

    public TaskToGetDTO(MessageToGetDTO messageToGetDTO,
                        LogToGetDTO logToGetDTO,
                        PropertyValueToGetDTO propertyValueToGetDTO) {
        this.messageToGetDTO = messageToGetDTO;
        this.logToGetDTO = logToGetDTO;
        this.propertyValueToGetDTO = propertyValueToGetDTO;
    }

    @Override
    public TaskGetDTO tranform(Task task) {
        if(task == null) return null;
        TaskGetDTO taskGet = new TaskGetDTO();
        BeanUtils.copyProperties(task, taskGet);
        try {
            taskGet.setLogs(task.getLogs().stream().map(logToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        try {
            taskGet.setComments(task.getComments().stream().map(messageToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) { }
        try {
            taskGet.setProperties(task.getProperties().stream().map(propertyValueToGetDTO::tranform).toList());
        } catch (NullPointerException ignore) {}
        return taskGet;
    }
}
