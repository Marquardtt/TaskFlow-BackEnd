package br.demo.backend.model.values;

import br.demo.backend.exception.DeserializerException;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.*;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.security.entity.UserDatailEntity;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.validation.constraints.Null;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
                    JsonNode jsonValue = jsonNode.get("value");
                    if (isPresent(jsonValue, "value")) {
                        JsonNode value = jsonValue.get("value");
                        Long idTaskVl = null;
                        try {
                            idTaskVl = jsonValue.get("id").asLong();
                        } catch (Exception e) {
                            System.out.println("Deu erro no id da propValue");
                        }

                        if (property.getType() == null) {
                            throw new DeserializerException("Property don't have property.getType() attribute");
                        }
                        System.out.println(jsonValue);
                        return switch (property.getType()) {
                            case USER -> deserializeUser(value, id, property, idTaskVl);
                            case TIME -> deserializeTime(value, id, property, idTaskVl);
                            case TEXT -> deserializeText(id, property, idTaskVl, value);
                            case NUMBER, PROGRESS -> deserializeNumberAndProgress(id, property, idTaskVl, value);
                            case ARCHIVE -> deserializeArchive(id, property, idTaskVl);
                            case DATE -> deserializeDate(value, id, property, idTaskVl);
                            case RADIO, SELECT -> deserializeUniOptioned(value, id, property, idTaskVl);
                            case CHECKBOX, TAG -> deserializeMutiOptioned(value, id, property, idTaskVl);
                        };
                    }

                    throw new DeserializerException("Value object dont have a value or Id!");
                }
                throw new DeserializerException("TaskValue don't have a value");
            }
            throw new DeserializerException("Property don't have property.getType() attribute or id");
        }
        throw new DeserializerException("TaskValue don't have a property");
    }

    private PropertyValue deserializeUser(JsonNode value, Long id, Property property, Long idTaskVl) {
        ArrayList<User> users = new ArrayList<>();
        for (JsonNode valueF : value) {
            if (isPresent(valueF, "id")) {
                Long idUser = valueF.get("id").asLong();
                User user = new User(idUser);

                UserDatailEntity userDetailsEntity = new UserDatailEntity();
                String username = valueF.get("username").asText();
                userDetailsEntity.setUsername(username);
                user.setUserDetailsEntity(userDetailsEntity);
                users.add(user);
            }
        }
        return new PropertyValue(id, property, new UserValued(idTaskVl, users));
    }

    private PropertyValue deserializeTime(JsonNode value, Long id, Property property, Long idTaskVl) {
        System.out.println(value);
        String color = value.get("color").asText();
        ArrayList<DateTimelines> starts = new ArrayList<>();
        System.out.println("color");
        // I have merda here
        Long idIntervals = value.get("id").asLong();
        System.out.println("id");

        for (JsonNode valueF : value.get("starts")) {
            DateTimelines date = new DateTimelines();
            if (isPresent(valueF, "id")) {
                date.setId(valueF.get("id").asLong());
            }
            date.setDate(OffsetDateTime.parse(valueF.get("date").asText()));
//                                    LocalDateTime.parse(valueF.asText())
            starts.add(date);
        }
        System.out.println("S");
        ArrayList<DateTimelines> ends = new ArrayList<>();
        for (JsonNode valueF : value.get("ends")) {
            DateTimelines date = new DateTimelines();
            if (isPresent(valueF, "id")) {
                date.setId(valueF.get("id").asLong());
            }
            date.setDate(OffsetDateTime.parse(valueF.get("date").asText()));
            ends.add(date);
        }
        System.out.println("end");
        JsonNode time = value.get("time");
        System.out.println("time");
        if (time.isNull()) {
            return new PropertyValue(id, property, new TimeValued(idTaskVl, new Intervals(idIntervals, null, starts, ends, color)));
        }
        Long idTime = null;
        if (!time.get("id").isNull()) {
            idTime = time.get("id").asLong();
        }
        Integer seconds = time.get("seconds").asInt();
        Integer minutes = time.get("minutes").asInt();
        Integer hours = time.get("hours").asInt();
        PropertyValue p = new PropertyValue(id, property, new TimeValued(idTaskVl,
                new Intervals(idIntervals, new Duration(idTime, seconds, minutes, hours), starts, ends, color)));
        System.out.println(p.getValue().getValue());
        return p;
    }

    private PropertyValue deserializeMutiOptioned(JsonNode value, Long id, Property property, Long idTaskVl) {
        ArrayList<Option> options = new ArrayList<>();
        for (JsonNode valueF : value) {
            if (isPresent(valueF, "id")) {
                String name = valueF.get("name").asText();
                Long idOpt = valueF.get("id").asLong();
                options.add(new Option(idOpt, name));

            }
        }

        return new PropertyValue(id, property, new MultiOptionValued(idTaskVl, options));
    }

    private PropertyValue deserializeUniOptioned(JsonNode value, Long id, Property property, Long idTaskVl) {
        if (isPresent(value, "id")) {
            Long idOpt = value.get("id").asLong();
            String name = value.get("name").asText();
            return new PropertyValue(id, property, new UniOptionValued(idTaskVl, new Option(idOpt, name)));
        }
        return new PropertyValue(id, property, new UniOptionValued(idTaskVl, null));
    }

    private static PropertyValue deserializeNumberAndProgress(Long id, Property property, Long idTaskVl, JsonNode value) {
        return new PropertyValue(id, property, new NumberValued(idTaskVl, value.asDouble()));
    }

    private static PropertyValue deserializeDate(JsonNode value, Long id, Property property, Long idTaskVl) {
        if (value.isNull()) {
            return new PropertyValue(id, property, new DateValued(idTaskVl, null));
        }
        return new PropertyValue(id, property, new DateValued(idTaskVl, OffsetDateTime.parse(value.asText())));
    }

    private static PropertyValue deserializeArchive(Long id, Property property, Long idTaskVl) {
        return new PropertyValue(id, property, new ArchiveValued(idTaskVl, null));
    }

    private static PropertyValue deserializeText(Long id, Property property, Long idTaskVl, JsonNode value) {
        return new PropertyValue(id, property, new TextValued(idTaskVl, value.asText()));
    }

    private Property deserializerProp(JsonNode jsonProp) {
        Long idprop = null;
        String name = jsonProp.get("name").asText();
        try {
            idprop = jsonProp.get("id").asLong();
        } catch (NullPointerException ignore) {
        }
        TypeOfProperty type = TypeOfProperty.valueOf(jsonProp.get("type").asText());
        return switch (type) {
            case USER, TIME, TEXT, NUMBER, PROGRESS, ARCHIVE -> new Limited(idprop, type, name);
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