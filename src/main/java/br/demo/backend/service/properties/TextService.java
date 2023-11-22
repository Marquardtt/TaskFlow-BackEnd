package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Text;
import br.demo.backend.repository.properties.TextRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TextService {

    TextRepository textRepository;

    public Collection<Text> findAll() {
        return textRepository.findAll();
    }

    public Text findOne(Long id) {
        return textRepository.findById(id).get();
    }

    public void save(Text text) {
        textRepository.save(text);
    }

    public void delete(Long id) {
        textRepository.deleteById(id);
    }
}
