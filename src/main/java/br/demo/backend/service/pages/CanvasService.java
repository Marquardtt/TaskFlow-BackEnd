package br.demo.backend.service.pages;


import br.demo.backend.model.pages.Canvas;
import br.demo.backend.repository.pages.CanvasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    CanvasRepository canvasRepository;

    public Collection<Canvas> findAll() {
        return canvasRepository.findAll();
    }

    public Canvas findOne(Long id) {
        return canvasRepository.findById(id).get();
    }

    public void save(Canvas canvasModel) {
        canvasRepository.save(canvasModel);
    }

    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
