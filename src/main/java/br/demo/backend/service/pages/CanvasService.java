package br.demo.backend.service.pages;


import br.demo.backend.model.Archive;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.repository.pages.CanvasRepository;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    private CanvasRepository canvasRepository;

    private TaskPageRepository taskPageRepository;
    private AutoMapper<Canvas> autoMapper;
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
        modelMapper.map(taskPage, oldTaskPage);
        taskPageRepository.save(oldTaskPage);
    }

    public void updateDraw(MultipartFile draw, Long id) {
        Canvas canvas = canvasRepository.findById(id).get();
        Archive archive = new Archive(draw);
        canvas.setDraw(archive);
        canvasRepository.save(canvas);
    }

    public void save(Canvas canvasModel) {
        canvasRepository.save(canvasModel);
    }

    public void update(Canvas canvasDto , Boolean patching) {
        Canvas canvas = patching ? canvasRepository.findById(canvasDto.getId()).get() : new Canvas();
        autoMapper.map(canvasDto, canvas, patching);
        canvasRepository.save(canvas);
    }

    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
