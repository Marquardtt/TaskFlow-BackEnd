package br.demo.backend.service.pages;

import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.Archive;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@AllArgsConstructor
@Service
public class PageService {

    private OrderedPageRepository orderedPageRepository;
    private CanvasPageRepository canvasPageRepository;
    private PageRepository pageRepository;
    private TaskCanvasRepository taskCanvasRepository;

    private AutoMapper<Page> autoMapper;
    private AutoMapper<OrderedPage> autoMapperOrdered;
    private AutoMapper<CanvasPage> autoMapperCanvas;
    private AutoMapper<TaskCanvas> autoMapperTaskCanvas;
    public Collection<Page> findAll() {
        Collection<Page> pages = pageRepository.findAll();
        return pages.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();

    }
    public Page findOne(Long id) {
        Page page = pageRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(page);
    }

    public OrderedPage updateIndexesVerifications(OrderedPage page, Long taskId, Integer index, Integer columnChaged) {
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        Option columnOption = (Option) taskOld.getTask().getProperties().stream().filter(p ->
                p.getProperty().equals(page.getPropertyOrdering())).findFirst().get().getValue().getValue();

        page.setTasks(page.getTasks().stream().map(t -> {
            TaskValue prop = t.getTask().getProperties().stream().filter(p ->
                    p.getProperty().equals(page.getPropertyOrdering()) &&
                            (p.getValue().getValue() == null && columnOption == null ||
                                    ((Option) p.getValue().getValue()).getId().equals(columnOption.getId()))
            ).findFirst().orElse(null);
            if (prop == null) return t;
            updateIndexesVerifications(taskOld, t, index, columnChaged);
            return t;
        }).toList());
        return (OrderedPage)ResolveStackOverflow.resolveStackOverflow(orderedPageRepository.save(page));
    }

    private void updateIndexesVerifications(TaskPage taskOld, TaskPage t, Integer index, Integer columnChaged){
        boolean verification = taskOld.getIndexAtColumn() > index || columnChaged == 1;
        if (t.equals(taskOld)) {
            t.setIndexAtColumn(index + (verification ? 0 : 1));
        } else {
            if ((t.getIndexAtColumn() >= index && verification) || t.getIndexAtColumn() > index) {
                t.setIndexAtColumn(t.getIndexAtColumn() + 1);
            }
        }
    }

    public OrderedPage updateIndexesVerifications(OrderedPage page, Long taskId, Integer index) {
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        page.setTasks(page.getTasks().stream().map(t -> {
            updateIndexesVerifications(taskOld, t, index, 0);
            return t;
        }).toList());
        return (OrderedPage) ResolveStackOverflow.resolveStackOverflow(orderedPageRepository.save(page));
    }


    public void update(OrderedPage orderedPageDto, Boolean patching) {
        OrderedPage orderedPage = patching ? orderedPageRepository.findById(orderedPageDto.getId()).get() : new OrderedPage();
        autoMapperOrdered.map(orderedPageDto, orderedPage, patching);
        orderedPageRepository.save(orderedPage);
    }
    public void update(CanvasPage canvasPageDto, Boolean patching) {
        CanvasPage canvasPage = patching ? canvasPageRepository.findById(canvasPageDto.getId()).get() : new CanvasPage();
        autoMapperCanvas.map(canvasPageDto, canvasPage, patching);
        canvasPageRepository.save(canvasPage);
    }
    public void update(Page pageDTO, Boolean patching) {
        Page page = patching ? pageRepository.findById(pageDTO.getId()).get() : new CanvasPage();
        autoMapper.map(pageDTO, page, patching);
        pageRepository.save(page);
    }

    public void save(OrderedPage canvasModel) {
        orderedPageRepository.save(canvasModel);
    }
    public void save(Page page) {
        pageRepository.save(page);
    }
    public void save(CanvasPage canvasPageModel) {
        canvasPageRepository.save(canvasPageModel);
    }


    public void delete(Long id) {
        orderedPageRepository.deleteById(id);
    }


    public void updateXAndY(TaskCanvas taskCanvas) {
        TaskCanvas oldTaskCanvas = taskCanvasRepository.findById(taskCanvas.getId()).get();
        autoMapperTaskCanvas.map(taskCanvas, oldTaskCanvas, true);
        taskCanvasRepository.save(oldTaskCanvas);
    }

    public void updateDraw(MultipartFile draw, Long id) {
        CanvasPage canvasPage = canvasPageRepository.findById(id).get();
        Archive archive = new Archive(draw);
        canvasPage.setDraw(archive);
        canvasPageRepository.save(canvasPage);
    }
}
