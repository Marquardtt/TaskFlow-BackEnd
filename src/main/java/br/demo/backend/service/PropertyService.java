package br.demo.backend.service;


import br.demo.backend.model.Property;
import br.demo.backend.repository.PropertyRepository;
import lombok.AllArgsConstructor;
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

    public void save(Property property) {
        propertyRepository.save(property);
    }

    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }
}
