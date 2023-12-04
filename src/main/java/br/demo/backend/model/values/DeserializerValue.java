package br.demo.backend.model.values;

import br.demo.backend.model.User;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeserializerValue extends StdDeserializer<Value> {
    JsonNode jsonNode;

    protected DeserializerValue() {
        super(Value.class);
    }

    @Override
    public Value deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (isPresent(jsonNode, "text")) {
            String jsonValue = jsonNode.get("text").asText();
            return new TextValued(id, jsonValue);
        } else if (isPresent(jsonNode, "dateTime")) {
            String jsonValue = jsonNode.get("dateTime").asText();
            LocalDateTime dateTime = null;
            dateTime = LocalDateTime.parse(jsonValue);
            return new DateValued(id, dateTime);

        } else if (isPresent(jsonNode, "number")) {
            Integer jsonValue = jsonNode.get("number").asInt();
            return new NumberValued(id, jsonValue);
        } else if (isPresent(jsonNode, "time")) {
            String jsonValue = jsonNode.get("time").asText();
            LocalTime time = LocalTime.parse(jsonValue);
            return new TimeValued(id, time);
        } else if (isPresent(jsonNode, "archive")) {
            String jsonValue = jsonNode.get("archive").asText();
            return new ArchiveValued(id, jsonValue);
        } else if (isPresent(jsonNode, "users")) {
            JsonNode usersJSON = jsonNode.get("users");
            List<User> users = new ArrayList<>();
            for (JsonNode user : usersJSON) {
                if (isPresent(user, "id")) {
                    Long idUser = user.get("id").asLong();
                    users.add(new User(idUser));
                }
            }
            return new UserValued(id, users);
        } else if (isPresent(jsonNode, "uniOption")) {
            JsonNode jsonValue = jsonNode.get("uniOption");
            Option option = null;
            if (isPresent(jsonValue, "id")) {
                option = new Option(jsonValue.get("id").asLong());
            }
            return new UniOptionValued(id, option);
        }
        JsonNode multiJSON = jsonNode.get("multiOptions");
        List<Option> options = new ArrayList<>();
        for (JsonNode option : multiJSON) {
            if (isPresent(option, "id")) {
                options.add(new Option(option.get("id").asLong()));
            }
        }
        return new MultiOptionValued(id, options);
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