package br.demo.backend.service;


import br.demo.backend.model.PropertyModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PropertyService {

    PropertyRepository propertyRepository;

    public Collection<PropertyModel> findAll() {
        return propertyRepository.findAll();
    }

    public PropertyModel findOne(Long id) {
        return propertyRepository.findById(id).get();
    }

    public void save(PropertyModel propertyModel) {
        propertyRepository.save(propertyModel);
    }

    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }
}
