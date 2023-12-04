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

    public DeserializerProperty() {
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

        if (isPresent(jsonNode, "type")) {
            TypeOfProperty type = TypeOfProperty.valueOf(jsonNode.get("type").asText());
            String name = null;
            Boolean visible = null;
            Boolean obligatory = null;
            if (isPresent(jsonNode, "name")) {
                name = jsonNode.get("name").asText();
            }
            if (isPresent(jsonNode, "visible")) {
                visible = jsonNode.get("visible").asBoolean();
            }
            if (isPresent(jsonNode, "obligatory")) {
                obligatory = jsonNode.get("obligatory").asBoolean();
            }
            if(type.equals(TypeOfProperty.DATE)){
                Boolean canBePass = null;
                Boolean includesHours = null;
                Boolean term = null;
                Boolean scheduling = null;
                if (isPresent(jsonNode, "canBePass")) {
                    canBePass = jsonNode.get("canBePass").asBoolean();
                }
                if (isPresent(jsonNode, "includesHours")) {
                    includesHours = jsonNode.get("includesHours").asBoolean();
                }
                if (isPresent(jsonNode, "term")) {
                    term = jsonNode.get("term").asBoolean();
                }
                if (isPresent(jsonNode, "scheduling")) {
                    scheduling = jsonNode.get("scheduling").asBoolean();
                }
                return new Date(id, name, visible, obligatory, canBePass, includesHours, term, scheduling);
            }
            else if (type.equals(TypeOfProperty.SELECT) ||
                    type.equals(TypeOfProperty.RADIO) ||
                    type.equals(TypeOfProperty.TAG) ||
                    type.equals(TypeOfProperty.CHECKBOX)) {
                List<Option> options = new ArrayList<>();
                if(isPresent(jsonNode, "options")){
                    List<JsonNode> optionsJson = jsonNode.findValues("options");
                    for(JsonNode optionJson : optionsJson){
                        if(isPresent(optionJson, "id")){
                            options.add(new Option(optionJson.get("id").asLong()));
                        }
                    }
                    System.out.println(options);
                }
                return new Select(id, name, visible, obligatory, options);
            }else{
                Integer maxSize = null;
                if(isPresent(jsonNode, "maximum")){
                    maxSize = jsonNode.get("maximum").asInt();
                }
                return new Limited(id, name, visible, obligatory, maxSize);
            }
        }
        throw new IllegalArgumentException("Invalid type of property");
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