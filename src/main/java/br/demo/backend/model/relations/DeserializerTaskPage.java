package br.demo.backend.model.relations;

import br.demo.backend.model.User;
import br.demo.backend.model.pages.Page;
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
            JsonNode jsonTask = jsonNode.findParent("task");
            if (isPresent(jsonTask, "id")) {
                Long taskId = jsonTask.findValue("id").asLong();
                if (isPresent(jsonNode, "x") && isPresent(jsonNode, "y")) {
                    Double x = jsonNode.findValue("x").asDouble();
                    Double y = jsonNode.findValue("y").asDouble();
                    return new TaskCanvas(id, new Task(taskId), x, y);
                }
                if (isPresent(jsonNode, "indexAtColumn")) {
                    Integer index = jsonNode.findValue("indexAtColumn").asInt();
                    return new TaskOrdered(id, new Task(taskId), index);
                }
                throw new RuntimeException("The subtype of TaskPage isn't clearly defined");
            }
            throw new RuntimeException("Task id not found");
        }
        throw new RuntimeException("Task not found");
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