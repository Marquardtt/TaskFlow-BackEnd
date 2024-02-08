package br.demo.backend.service.pages;


import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.repository.pages.CanvasRepository;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    private CanvasRepository canvasRepository;
    private TaskPageRepository taskPageRepository;
    private ModelMapper modelMapper;
    public Collection<Canvas> findAll() {
        Collection<Canvas> canvas = canvasRepository.findAll();
        return canvas.stream().map(c -> (Canvas)ResolveStackOverflow.resolveStackOverflow(c)).toList();
    }

    public Canvas findOne(Long id) {
        Canvas canvas = canvasRepository.findById(id).get();
        return (Canvas)ResolveStackOverflow.resolveStackOverflow(canvas);
    }

    public void updateXAndY(TaskPage taskPage) {
        TaskPage oldTaskPage = taskPageRepository.findById(taskPage.getId()).get();
        taskPageRepository.save(taskPage);
        modelMapper.map(taskPage, oldTaskPage);
        taskPageRepository.save(oldTaskPage);
    }

    public void updateDraw(Canvas canvas) {
        Canvas oldCanvas = canvasRepository.findById(canvas.getId()).get();
        oldCanvas.setDraw(canvas.getDraw());
        canvasRepository.save(canvas);
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
