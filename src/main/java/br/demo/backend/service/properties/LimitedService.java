package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Limited;
import br.demo.backend.repository.properties.LimitedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LimitedService {

    LimitedRepository limitedRepository;

    public Collection<Limited> findAll() {
        return limitedRepository.findAll();
    }

    public Limited findOne(Long id) {
        return limitedRepository.findById(id).get();
    }

    public void save(Limited limited) {
        limitedRepository.save(limited);
    }

    public void delete(Long id) {
        limitedRepository.deleteById(id);
    }
}
