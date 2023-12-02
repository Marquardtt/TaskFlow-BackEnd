package br.demo.backend.service.properties;


import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.PropertyPostDTO;
import br.demo.backend.model.properties.PropertyPutDTO;
import br.demo.backend.repository.properties.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PropertyService {

    PropertyRepository propertyRepository;

    public Collection<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property findOne(Long id) {
        return propertyRepository.findById(id).get();
    }

    public void save(PropertyPostDTO propertyPostDTO) {
        Property property = new Property();
        BeanUtils.copyProperties(propertyPostDTO, property);
        propertyRepository.save(property);
    }

    public void save(PropertyPutDTO propertyPostDTO) {
        Property property = new Property();
        BeanUtils.copyProperties(propertyPostDTO, property);
        propertyRepository.save(property);
    }
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }
}
