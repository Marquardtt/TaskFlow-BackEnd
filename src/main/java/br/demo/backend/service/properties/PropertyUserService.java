package br.demo.backend.service.properties;

import br.demo.backend.model.properties.PropertyUser;
import br.demo.backend.repository.PropertyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PropertyUserService {

    PropertyUserRepository propertyUserRepository;

    public Collection<PropertyUser> findAll() {
        return propertyUserRepository.findAll();
    }

    public PropertyUser findOne(Long id) {
        return propertyUserRepository.findById(id).get();
    }

    public void save(PropertyUser propertyUser) {
        propertyUserRepository.save(propertyUser);
    }

    public void delete(Long id) {
        propertyUserRepository.deleteById(id);
    }
}
