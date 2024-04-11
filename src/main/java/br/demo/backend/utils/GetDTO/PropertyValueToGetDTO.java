package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import br.demo.backend.model.dtos.relations.UserValuedGetDTO;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.values.UserValued;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

public class PropertyValueToGetDTO implements ModelToGetDTO<PropertyValue, PropertyValueGetDTO> {

    private final PropertyToGetDTO propertyToGetDTO;
    private final OtherUsersToDTO otherUsersToDTO;

    public PropertyValueToGetDTO(PropertyToGetDTO propertyToGetDTO, OtherUsersToDTO otherUsersToDTO) {
        this.propertyToGetDTO = propertyToGetDTO;
        this.otherUsersToDTO = otherUsersToDTO;
    }

    @Override
    public PropertyValueGetDTO tranform(PropertyValue propertyValue) {
        if(propertyValue == null) return null;
        PropertyValueGetDTO taskValue = new PropertyValueGetDTO();
        BeanUtils.copyProperties(propertyValue, taskValue);
        taskValue.setProperty(propertyToGetDTO.tranform(propertyValue.getProperty()));
        if(propertyValue.getValue() instanceof UserValued userValued){
            try{
                UserValuedGetDTO userValuedGet = new UserValuedGetDTO();
                userValuedGet.setValue(((Collection<User>)userValued.getValue())
                        .stream().map(otherUsersToDTO::tranform).toList());
                userValuedGet.setId(userValued.getId());
                taskValue.setValue(userValuedGet);
            }catch (NullPointerException ignore) {}
        }
        return taskValue;
    }
}
