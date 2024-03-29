package br.demo.backend.model.values;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.PropertyValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeserializerValue extends StdDeserializer<PropertyValue> {
    JsonNode jsonNode;


    protected DeserializerValue() {
        super(PropertyValue.class);
    }

    @Override
    public PropertyValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        if (isPresent(jsonNode, "property")) {
            JsonNode jsonProp = jsonNode.get("property");
            if (isPresent(jsonProp, "type")) {
                String type = jsonProp.get("type").asText();
                Long idProp = null;
                try {
                    idProp = jsonProp.get("id").asLong();
                } catch (Exception e) {
                    System.out.println("Deu erro no id da prop");
                }
                if (isPresent(jsonNode, "value")) {
                    JsonNode jsonValue = jsonNode.get("value");
                    if (isPresent(jsonValue, "value")) {
                        JsonNode value = jsonValue.get("value");
                        Long idTaskVl = null;
                        try {
                            idTaskVl = jsonValue.get("id").asLong();
                        } catch (Exception e) {
                            System.out.println("Deu erro no id da propValue");
                        }
                        Property property = new Property(idProp, TypeOfProperty.valueOf(type));
                        System.out.println(property);
                        if (type.equals("TEXT")) {
                            return new PropertyValue(id, property,  new TextValued(idTaskVl, value.asText()));
                        }
                        else if(type.equals("ARCHIVE")){
                            return new PropertyValue(id, property,  new ArchiveValued(idTaskVl, null));
                        }
                        else if(type.equals("DATE")){
                            if(value.isNull()){
                                return new PropertyValue(id, property,  new DateValued(idTaskVl, null));
                            }
                            return new PropertyValue(id, property,  new DateValued(idTaskVl, LocalDateTime.parse(value.asText())));

                        }
                        else if(type.equals("NUMBER") || type.equals("PROGRESS")){
                            return new PropertyValue(id, property, new NumberValued(idTaskVl, value.asDouble()));
                        }
                        else if(type.equals("RADIO") || type.equals("SELECT")){
                            if(isPresent(value, "id")){
                                Long idOpt = value.get("id").asLong();
                                String name = value.get("name").asText();
                                return new PropertyValue(id, property,  new UniOptionValued(idTaskVl, new Option(idOpt, name)));
                            }
                            return new PropertyValue(id, property, new UniOptionValued(idTaskVl, null));
                        }
                        else if(type.equals("CHECKBOX") || type.equals("TAG")){
                            ArrayList<Option> options = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "id")){
                                    String name = value.get("name").asText();
                                    Long idOpt = value.get("id").asLong();
                                    options.add(new Option(idOpt, name));
                                }
                            }
                            return new PropertyValue(id, new Property(idProp), new MultiOptionValued(idTaskVl, options));
                        }
                        else if(type.equals("TIME")){
                            String color = value.get("color").asText();
                            ArrayList<LocalDateTime> starts = new ArrayList<>();
                            Long idIntervals = value.get("id").asLong();
                            for(JsonNode valueF : value.get("starts")){
                                starts.add(LocalDateTime.parse(valueF.asText()));
                            }
                            ArrayList<LocalDateTime> ends = new ArrayList<>();
                            for(JsonNode valueF : value.get("ends")){
                                ends.add(LocalDateTime.parse(valueF.asText()));
                            }
                            JsonNode time = value.get("time");
                            if(time.isNull()){
                                return new PropertyValue(id, property, new TimeValued(idTaskVl, new Intervals(idIntervals, null, starts, ends, color)));
                            }
                            return new PropertyValue(id, property, new TimeValued(idTaskVl, new Intervals(idIntervals, Duration.parse(time.asText()), starts, ends, color)));
                        }
                        //TODO: username virara id de novo e username devera ser pego de userdetailsentity
                        else if(type.equals("USER")){
                            ArrayList<User> users = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "username")){
                                    users.add(new User(valueF.get("username").asText()));
                                }
                            }
                            return new PropertyValue(id, new Property(idProp), new UserValued(idTaskVl, users));
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