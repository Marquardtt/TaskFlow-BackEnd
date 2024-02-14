package br.demo.backend.model.values;

import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeserializerValue extends StdDeserializer<TaskValue> {
    JsonNode jsonNode;


    protected DeserializerValue() {
        super(TaskValue.class);
    }

    @Override
    public TaskValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        if (isPresent(jsonNode, "property")) {
            JsonNode jsonProp = jsonNode.get("property");
            if (isPresent(jsonProp, "type") &&
                    isPresent(jsonProp, "id")) {
                String type = jsonProp.get("type").asText();
                Long idProp = jsonProp.get("id").asLong();

                if (isPresent(jsonNode, "value")) {
                    JsonNode jsonValue = jsonNode.get("value");
                    if (isPresent(jsonValue, "value") &&
                            isPresent(jsonValue, "id")) {
                        JsonNode value = jsonValue.get("value");
                        Long idTaskVl = jsonValue.get("id").asLong();
                        Property property = new Property(idProp);

                        if (type.equals("TEXT")) {
                            return new TaskValue(id, property, new TextValued(idTaskVl, value.asText()));
                        }else if(type.equals("ARCHIVE")){
                            return new TaskValue(id, property, new ArchiveValued(idTaskVl, null));
                        }
                        else if(type.equals("DATE")){
                            return new TaskValue(id, property, new DateValued(idTaskVl, LocalDateTime.parse(value.asText())));
                        }
                        else if(type.equals("NUMBER") || type.equals("PROGRESS")){
                            return new TaskValue(id, property, new NumberValued(idTaskVl, value.asInt()));
                        }
                        else if(type.equals("RADIO") || type.equals("SELECT")){
                            if(isPresent(value, "id")){
                                Long idOpt = value.get("id").asLong();
                                return new TaskValue(id, property, new UniOptionValued(idTaskVl, new Option(idOpt)));
                            }
                            return new TaskValue(id, property, new UniOptionValued(idTaskVl, null));
                        }
                        else if(type.equals("CHECKBOX") || type.equals("TAG")){
                            ArrayList<Option> options = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "id")){
                                    options.add(new Option(valueF.get("id").asLong()));
                                }
                            }
                            return new TaskValue(id, new Property(idProp), new MultiOptionValued(idTaskVl, options));
                        }
                        else if(type.equals("TIME")){
                            return new TaskValue(id, property, new TimeValued(idTaskVl, Duration.parse(value.asText())));
                        }
                        else if(type.equals("USER")){
                            ArrayList<User> users = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "id")){
                                    users.add(new User(valueF.get("id").asText()));
                                }
                            }
                            return new TaskValue(id, new Property(idProp), new UserValued(idTaskVl, users));
                        }
                        throw new RuntimeException("Property have a unknown type");
                    }
                    throw new RuntimeException("Value object dont have a value or Id!");
                }
                throw new RuntimeException("TaskValue don't have a value");
            }
                throw new RuntimeException("Property don't have type attribute or id");
        }
            throw new RuntimeException("TaskValue don't have a property");
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