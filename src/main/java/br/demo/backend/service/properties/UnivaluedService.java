package br.demo.backend.service.properties;

import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.ids.ValueId;
import br.demo.backend.repository.properties.UnivaluedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UnivaluedService {

    UnivaluedRepository univaluedRepository;

    public Collection<Univalued> findAll() {
        return univaluedRepository.findAll();
    }

    public Univalued findOne(Long taskId, Long propertyId) {
        return univaluedRepository.findById(new ValueId(propertyId, taskId)).get();
    }

    public void save(Univalued univalued) {
        univaluedRepository.save(univalued);
    }

    public void delete(Long taskId, Long propertyId) {
        univaluedRepository.deleteById(new ValueId(propertyId, taskId));
    }
}
