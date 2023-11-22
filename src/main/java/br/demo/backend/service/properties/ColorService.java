package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Color;
import br.demo.backend.repository.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ColorService {

    ColorRepository colorRepository;

    public Collection<Color> findAll() {
        return colorRepository.findAll();
    }

    public Color findOne(Long id) {
        return colorRepository.findById(id).get();
    }

    public void save(Color color) {
        colorRepository.save(color);
    }

    public void delete(Long id) {
        colorRepository.deleteById(id);
    }
}
