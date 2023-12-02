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

        if (isPresent("text")) {
            String valorJson = jsonNode.get("text").asText();
            return new TextValued(id, valorJson);
        } else if (isPresent("data")) {
            String valorJson = jsonNode.get("data").asText();
            LocalDateTime date = LocalDateTime.parse(valorJson);
            return new DateValued(id, date);
        } else if (isPresent("number")) {
            Integer valorJson = jsonNode.get("number").asInt();
            return new NumberValued(id, valorJson);
        } else if (isPresent("tempo")) {
            String valorJson = jsonNode.get("tempo").asText();
            LocalTime time = LocalTime.parse(valorJson);
            return new TimeValued(id, time);
        } else if (isPresent("archive")) {
            String valorJson = jsonNode.get("archive").asText();
            return new ArchiveValued(id, valorJson);
        } else if (isPresent("users")) {
            List<JsonNode> usersJSON = jsonNode.findValues("users");
            List<User> users = new ArrayList<>();
            for (JsonNode user : usersJSON) {
                Long idUser = user.get("id").asLong();
                users.add(new User(idUser));
            }
            return new UserValued(id, users);
        } else if (isPresent("uniOption")) {
            JsonNode valorJson = jsonNode.findValue("uniOption");
            Option option = new Option(valorJson.get("id").asLong());
            return new UniOptionValued(id, option);
        }
        List<JsonNode> multiJSON = jsonNode.findValues("multiOptions");
        List<Option> options = new ArrayList<>();
        for (JsonNode option : multiJSON) {
            options.add(new Option(option.get("id").asLong()));
        }
        return new MultiOptionValued(id, options);
    }

    private boolean isPresent(String text) {
        try {
            if (jsonNode.findParent(text) != null) {
                System.out.println(jsonNode.findParent(text));
                return true;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return false;
        }
    }
}