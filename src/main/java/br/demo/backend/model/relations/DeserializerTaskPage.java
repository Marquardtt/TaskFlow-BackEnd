package br.demo.backend.model.relations;

import br.demo.backend.model.User;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class DeserializerTaskPage extends StdDeserializer<TaskPage> {
    JsonNode jsonNode;


    protected DeserializerTaskPage() {
        super(TaskPage.class);
    }

    @Override
    public TaskPage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (isPresent(jsonNode, "task")) {
            JsonNode jsonTask = jsonNode.get("task");
            if (isPresent(jsonTask, "id")) {
                Long idTask = jsonTask.get("id").asLong();
                Task task = new Task(idTask);
                if(isPresent(jsonNode, "x") &&
                        isPresent(jsonNode, "y")){
                    Double x = jsonNode.get("x").asDouble();
                    Double y = jsonNode.get("x").asDouble();
                    return new TaskCanvas(id, task, x, y);
                }
                if(isPresent(jsonNode, "index")){
                    Integer index = jsonNode.get("index").asInt();
                    return new TaskOrdered(id, task, index);
                }
                throw new RuntimeException("Subclass isn't clearly defined");
            }
            throw new RuntimeException("Task id is not present");
        }
        throw new RuntimeException("Task is not present");
    }

    private boolean isPresent(JsonNode jsonNode, String text) {
        try {
            if (jsonNode.findParent(text) != null) {
                return true;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return false;
        }

    }
}