package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Multivalued;
import br.demo.backend.repository.MultivaluedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MultivaluedService {

    private MultivaluedRepository canvasRepository;

    public Collection<Multivalued> findAll() {
        return canvasRepository.findAll();
    }

    public Multivalued findOne(Long id) {
        return canvasRepository.findById(id).get();
    }

    public void save(Multivalued multivalued) {
        canvasRepository.save(multivalued);
    }

    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
