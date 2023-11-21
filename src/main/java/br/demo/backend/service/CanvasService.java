package br.demo.backend.service;


import br.demo.backend.model.pages.CanvasModel;
import br.demo.backend.repository.CanvasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    CanvasRepository canvasRepository;

    public Collection<CanvasModel> findAll() {
        return canvasRepository.findAll();
    }

    public CanvasModel findOne(Long id) {
        return canvasRepository.findById(id).get();
    }

    public void save(CanvasModel canvasModel) {
        canvasRepository.save(canvasModel);
    }

    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
