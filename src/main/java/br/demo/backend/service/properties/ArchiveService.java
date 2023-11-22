package br.demo.backend.service.properties;


import br.demo.backend.model.properties.Archive;
import br.demo.backend.repository.properties.ArchiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ArchiveService {

    private ArchiveRepository archiveRepository;

    public Collection<Archive> findAll() {
        return archiveRepository.findAll();
    }

    public Archive findOne(Long id) {
        return archiveRepository.findById(id).get();
    }

    public void save(Archive archive) {
        archiveRepository.save(archive);
    }

    public void delete(Long id) {
        archiveRepository.deleteById(id);
    }
}
