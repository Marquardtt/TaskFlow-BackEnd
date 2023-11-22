package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Progress;
import br.demo.backend.repository.properties.ProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ProgressService {

    ProgressRepository progressRepository;

    public Collection<Progress> findAll() {
        return progressRepository.findAll();
    }

    public Progress findOne(Long id) {
        return progressRepository.findById(id).get();
    }

    public void save(Progress progress) {
        progressRepository.save(progress);
    }

    public void delete(Long id) {
        progressRepository.deleteById(id);
    }
}
