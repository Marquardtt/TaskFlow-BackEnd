package br.demo.backend.service.pages;


import br.demo.backend.model.pages.Canvas;
import br.demo.backend.repository.pages.CanvasRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    private CanvasRepository canvasRepository;
    private TaskService taskService;

    public Collection<Canvas> findAll() {
        Collection<Canvas> canvas = canvasRepository.findAll();
        for(Canvas canvasModel : canvas) {
            ResolveStackOverflow.resolveStackOverflow(canvasModel);
        }
        return canvas;
    }

    public Canvas findOne(Long id) {
        Canvas canvas = canvasRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(canvas);
        return canvas;
    }

    public void save(Canvas canvasModel) {
        canvasRepository.save(canvasModel);
    }

    public void update(Canvas canvas) {
        canvasRepository.save(canvas);
    }

    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
