package br.demo.backend.model.pages;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.DeserializerProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.hibernate.sql.results.graph.collection.CollectionLoadingLogger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DeserializePage extends StdDeserializer<Page> {
    JsonNode jsonNode;

    protected DeserializePage() {
        super(Page.class);
    }

    @Override
    public Page deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (isPresent(jsonNode, "type")) {
            TypeOfPage type = TypeOfPage.valueOf(jsonNode.get("type").asText());
            String name = null;
            Collection<Property> properties = new HashSet<>();

            if (isPresent(jsonNode, "name")) {
                name = jsonNode.get("name").asText();
            }
            if (isPresent(jsonNode, "properties")) {
                JsonNode propsJsN = jsonNode.get("properties");
                for (JsonNode jsN : propsJsN) {
                    DeserializerProperty dsP = new DeserializerProperty();
                    properties.add(dsP.deserialize(jsN.traverse(), deserializationContext));
                }
            }
            if (type.equals(TypeOfPage.CANVAS)) {
                Collection<TaskCanvas> tasks = new HashSet<>();
                String draw = null;
                if (isPresent(jsonNode, "tasks")) {
                    JsonNode tasksJsN = jsonNode.get("tasks");
                    for (JsonNode jsN : tasksJsN) {
                        Double x = jsN.get("x").asDouble();
                        Double y = jsN.get("y").asDouble();
                        JsonNode taskJsN = jsN.get("task");
                        Task task = new Task(taskJsN.get("id").asLong());
                        tasks.add(new TaskCanvas(null, task, x, y));
                    }
                }
                if(isPresent(jsonNode, "draw")) {
                    draw = jsonNode.get("draw").asText();
                }
                return new Canvas(id, name, properties, draw, tasks);
            }
            Collection<Task> tasks = new HashSet<>();
            Property propertyOrdering = null;

            if (isPresent(jsonNode, "tasks")) {
                JsonNode tasksJsN = jsonNode.get("tasks");
                for (JsonNode jsN : tasksJsN) {
                    Task task = new Task(jsN.get("id").asLong());
                    tasks.add(task);
                }
            }
            if (isPresent(jsonNode, "propertyOrdering")) {
                JsonNode propOrderingJsN = jsonNode.get("propertyOrdering");
                try{
                    DeserializerProperty dsP = new DeserializerProperty();
                    propertyOrdering = dsP.deserialize(propOrderingJsN.traverse(), deserializationContext);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return new CommonPage(id, name, type, properties, tasks, propertyOrdering);
        }
        throw new IllegalArgumentException("There's no type in the page");
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