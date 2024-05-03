package br.demo.backend.model.values;

import br.demo.backend.exception.DeserializerException;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.*;
import br.demo.backend.model.relations.PropertyValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.validation.constraints.Null;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                Property property = deserializerProp(jsonProp);
                if (isPresent(jsonNode, "value")) {
                    System.out.println(jsonNode);
                    JsonNode jsonValue = jsonNode.get("value");
                    if (isPresent(jsonValue, "value")) {
                        JsonNode value = jsonValue.get("value");
                        Long idTaskVl = null;
                        try {
                            idTaskVl = jsonValue.get("id").asLong();
                        } catch (Exception e) {
                            System.out.println("Deu erro no id da propValue");
                        }

                        if (property.getType().equals(TypeOfProperty.TEXT)) {
                            return new PropertyValue(id, property,  new TextValued(idTaskVl, value.asText()));
                        }
                        else if(property.getType().equals(TypeOfProperty.ARCHIVE)){
                            return new PropertyValue(id, property,  new ArchiveValued(idTaskVl, null));
                        }
                        else if(property.getType().equals(TypeOfProperty.DATE)){
                            if(value.isNull()){
                                return new PropertyValue(id, property,  new DateValued(idTaskVl, null));
                            }
                            return new PropertyValue(id, property,  new DateValued(idTaskVl, LocalDateTime.parse(value.asText())));

                        }
                        else if(property.getType().equals(TypeOfProperty.NUMBER) || property.getType().equals(TypeOfProperty.PROGRESS)){
                            return new PropertyValue(id, property, new NumberValued(idTaskVl, value.asDouble()));
                        }
                        else if(property.getType().equals(TypeOfProperty.RADIO) || property.getType().equals(TypeOfProperty.SELECT)){
                            if(isPresent(value, "id")){
                                Long idOpt = value.get("id").asLong();
                                String name = value.get("name").asText();
                                return new PropertyValue(id, property,  new UniOptionValued(idTaskVl, new Option(idOpt, name)));
                            }
                            return new PropertyValue(id, property, new UniOptionValued(idTaskVl, null));
                        }
                        else if(property.getType().equals(TypeOfProperty.CHECKBOX) || property.getType().equals(TypeOfProperty.TAG)){
                            ArrayList<Option> options = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "id")){
                                    String name = valueF.get("name").asText();
                                    Long idOpt = valueF.get("id").asLong();
                                    options.add(new Option(idOpt, name));

                                }
                            }

                            return new PropertyValue(id, property, new MultiOptionValued(idTaskVl, options));
                        }
                        else if(property.getType().equals(TypeOfProperty.TIME)){
                            String color = value.get("color").asText();
                            ArrayList<DateTimelines> starts = new ArrayList<>();
                            // I have merda here
                            Long idIntervals = value.get("id").asLong();
                                for(JsonNode valueF : value.get("starts")){
                                       DateTimelines date = new DateTimelines();
                                    if (isPresent(valueF, "id")){
                                        date.setId(valueF.get("id").asLong());
                                    }
                                       date.setDate(LocalDateTime.parse(valueF.get("date").asText()));
//                                    LocalDateTime.parse(valueF.asText())
                                    starts.add(date);
                                }
                            ArrayList<DateTimelines> ends = new ArrayList<>();
                                for(JsonNode valueF : value.get("ends")){
                                    DateTimelines date = new DateTimelines();
                                    if (isPresent(valueF, "id")){
                                        date.setId(valueF.get("id").asLong());
                                    }
                                    date.setDate(LocalDateTime.parse(valueF.get("date").asText()));
                                    ends.add(date);
                                }
                            JsonNode time = value.get("time");
                            if(time.isNull()){
                                return new PropertyValue(id, property, new TimeValued(idTaskVl, new Intervals(idIntervals, null, starts, ends, color)));
                            }
                            Long idTime = null;
                            if (!time.get("id").isNull()){
                                idTime = time.get("id").asLong();
                            }
                            Integer seconds = time.get("seconds").asInt();
                            Integer minutes = time.get("minutes").asInt();
                            Integer hours = time.get("hours").asInt();
                            return new PropertyValue(id, property, new TimeValued(idTaskVl,
                                    new Intervals(idIntervals, new Duration(idTime, seconds, minutes, hours), starts, ends, color)));
                        }

                        else if(property.getType().equals(TypeOfProperty.USER)){
                            ArrayList<User> users = new ArrayList<>();
                            for(JsonNode valueF : value){
                                if(isPresent(valueF, "id")){
                                    Long idUser = valueF.get("id").asLong();
                                    users.add(new User(idUser));
                                }
                            }
                            return new PropertyValue(id, property, new UserValued(idTaskVl, users));
                        }
                        throw new DeserializerException("Property have a unknown type)");
                    }
                    throw new DeserializerException("Value object dont have a value or Id!");
                }
                throw new DeserializerException("TaskValue don't have a value");
            }
                throw new DeserializerException("Property don't have property.getType() attribute or id");
        }
            throw new DeserializerException("TaskValue don't have a property");
    }
    
    private Property deserializerProp (JsonNode jsonProp){
        Long idprop = null;
        String name = jsonProp.get("name").asText();
        try{
            idprop = jsonProp.get("id").asLong();
        }catch (NullPointerException ignore){}
        TypeOfProperty type = TypeOfProperty.valueOf(jsonProp.get("type").asText());
        return switch (type){
            case USER, TIME,  TEXT, NUMBER, PROGRESS, ARCHIVE -> new Limited(idprop, type, name);
            case DATE -> new Date(idprop, type, name);
            default -> new Select(idprop, type, name);
        };
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