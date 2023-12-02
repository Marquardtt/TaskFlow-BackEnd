package br.demo.backend.model.properties;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeserializerProperty extends StdDeserializer<Property> {
    JsonNode jsonNode;

    protected DeserializerProperty() {
        super(Property.class);
    }

    @Override
    public Property deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        Long id = null;
        jsonNode = deserializationContext.readTree(jsonParser);
        try {
            id = jsonNode.get("id").asLong();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (isPresent("type")) {
            TypeOfProperty type = TypeOfProperty.valueOf(jsonNode.get("type").asText());
            Page page = null;
            String name = null;
            Boolean visible = null;
            Boolean obligatory = null;

            if (isPresent("name")) {
                name = jsonNode.get("name").asText();
            }
            if (isPresent("visible")) {
                visible = jsonNode.get("visible").asBoolean();
            }
            if (isPresent("obligatory")) {
                obligatory = jsonNode.get("obligatory").asBoolean();
            }
            if (isPresent("page")) {
                JsonNode pageJson = jsonNode.get("page");
                page = new Page(pageJson.get("id").asLong());
            }
            if(type.equals(TypeOfProperty.DATE)){
                Boolean canBePass = null;
                Boolean includesHours = null;
                Boolean term = null;
                Boolean scheduling = null;
                if (isPresent("canBePass")) {
                    canBePass = jsonNode.get("canBePass").asBoolean();
                }
                if (isPresent("includesHours")) {
                    includesHours = jsonNode.get("includesHours").asBoolean();
                }
                if (isPresent("term")) {
                    term = jsonNode.get("term").asBoolean();
                }
                if (isPresent("scheduling")) {
                    scheduling = jsonNode.get("scheduling").asBoolean();
                }
                return new Date(id, name, visible, obligatory, page, canBePass, includesHours, term, scheduling);
            }
            else if (type.equals(TypeOfProperty.SELECT)) {
                if(isPresent("options")){
                    List<JsonNode> optionsJson = jsonNode.findValues("options");
                    List<Option> options = new ArrayList<>();
                    for(JsonNode optionJson : optionsJson){
                        options.add(new Option(optionJson.get("id").asLong()));
                    }
                    return new Select(id, name, visible, obligatory, page, options);
                }
            }else{
                if(isPresent("maximum")){
                    Integer maxSize = jsonNode.get("maximum").asInt();
                    return new Limited(id, name, visible, obligatory, page, maxSize);
                }
            }

        }
        throw new IllegalArgumentException("Invalid type of property");

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