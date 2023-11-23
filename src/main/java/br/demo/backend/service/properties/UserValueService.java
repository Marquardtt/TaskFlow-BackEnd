package br.demo.backend.service.properties;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.model.properties.relations.ids.ValueId;
import br.demo.backend.repository.properties.UserValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserValueService {

    UserValueRepository userValueRepository;

    public Collection<UserValue> findAll() {
        return userValueRepository.findAll();
    }

    public UserValue findOne(Long taskId, Long propertyId) {
        return userValueRepository.findById(new ValueId(propertyId, taskId)).get();
    }

    public void save(UserValue userValue) {
        userValueRepository.save(userValue);
    }

    public void delete(Long taskId, Long propertyId) {
        userValueRepository.deleteById(new ValueId(propertyId, taskId));
    }
}
