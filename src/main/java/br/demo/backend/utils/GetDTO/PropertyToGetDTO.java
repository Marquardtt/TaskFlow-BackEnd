package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class PropertyToGetDTO implements ModelToGetDTO<Property, PropertyGetDTO> {
    @Override
    public PropertyGetDTO tranform(Property property) {
        if(property == null) return null;
        PropertyGetDTO propertyGet;
        if(property instanceof Date){
            propertyGet = new DateGetDTO();
        }else if(property instanceof Limited){
            propertyGet = new LimitedGetDTO();
        }else {
            propertyGet = new SelectGetDTO();
        }
        BeanUtils.copyProperties(property, propertyGet);
        return propertyGet;
    }
}
