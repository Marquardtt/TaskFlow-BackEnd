package br.demo.backend.service.properties;

import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.ids.ValueId;
import br.demo.backend.repository.properties.MultivaluedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MultivaluedService {

    MultivaluedRepository multivaluedRepository;

    public Collection<Multivalued> findAll() {
        return multivaluedRepository.findAll();
    }

    public Multivalued findOne(Long taskId, Long propertyId) {
        return multivaluedRepository.findById(new ValueId(propertyId, taskId)).get();
    }

    public void save(Multivalued multivalued) {
        multivaluedRepository.save(multivalued);
    }

    public void delete(Long taskId, Long propertyId) {
        multivaluedRepository.deleteById(new ValueId(propertyId, taskId));
    }
}
