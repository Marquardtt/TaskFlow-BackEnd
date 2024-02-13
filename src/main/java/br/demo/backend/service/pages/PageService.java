package br.demo.backend.service.pages;

import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.OtherPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.OtherPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Service
public class PageService {

    private OrderedPageRepository orderedPageRepository;
    private CanvasPageRepository canvasPageRepository;
    private PageRepository pageRepository;
    private TaskCanvasRepository taskCanvasRepository;
    private OtherPageRepository otherPageRepository;
    private SelectRepository selectRepository;
    private DateRepository dateRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;

    private AutoMapper<OrderedPage> autoMapperOrdered;
    private AutoMapper<CanvasPage> autoMapperCanvas;
    private AutoMapper<TaskCanvas> autoMapperTaskCanvas;
    private AutoMapper<OtherPage> autoMapperOther;
    public Collection<? extends Page> findAll() {
        Collection<Page> pages = pageRepository.findAll();
        return pages.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }
    public Page findOne(Long id) {
        Page page = pageRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(page);
    }

    public OrderedPage updateIndex(OrderedPage page, Long taskId, Integer index, Integer columnChaged) {
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
            updateIndexVerification((TaskOrdered)taskOld, (TaskOrdered) t, index, columnChaged);
            return t;
        }).toList());
        return (OrderedPage)ResolveStackOverflow.resolveStackOverflow(orderedPageRepository.save(page));
    }

    private void updateIndexVerification(TaskOrdered taskOld, TaskOrdered t, Integer index, Integer columnChaged){
        boolean verification = taskOld.getIndexAtColumn() > index || columnChaged == 1;
        if (t.equals(taskOld)) {
            t.setIndexAtColumn(index + (verification ? 0 : 1));
        } else {
            if ((t.getIndexAtColumn() >= index && verification) || t.getIndexAtColumn() > index) {
                t.setIndexAtColumn(t.getIndexAtColumn() + 1);
            }
        }
    }

    public OtherPage updateIndex(OtherPage page, Long taskId, Integer index) {
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        page.setTasks(page.getTasks().stream().map(t -> {
            updateIndexVerification((TaskOrdered) taskOld, (TaskOrdered) t, index, 0);
            return t;
        }).toList());
        return (OtherPage) ResolveStackOverflow.resolveStackOverflow(otherPageRepository.save(page));
    }


    public void update(OrderedPage orderedPageDto, Boolean patching) {
        OrderedPage orderedPage = patching ? orderedPageRepository.findById(orderedPageDto.getId()).get() : new OrderedPage();
        autoMapperOrdered.map(orderedPageDto, orderedPage, patching);
        orderedPageRepository.save(orderedPage);
    }
    public void update(CanvasPage canvasPageDto, Boolean patching) {
        CanvasPage  canvasPage = patching ? canvasPageRepository.findById(canvasPageDto.getId()).get() : new CanvasPage();
        autoMapperCanvas.map(canvasPageDto, canvasPage, patching);
        canvasPageRepository.save(canvasPage);
    }
    public void update(OtherPage otherPageDto, Boolean patching) {
        OtherPage otherPage = patching ? otherPageRepository.findById(otherPageDto.getId()).get() : new OtherPage();
        autoMapperOther.map(otherPageDto, otherPage, patching);
        otherPageRepository.save(otherPage);
    }


    public void save(OrderedPage canvasModel) {
        OrderedPage page = orderedPageRepository.save(canvasModel);
        Property propOrdering = page.getType().equals(TypeOfPage.KANBAN) ?
                propOrdSelect(page) : propOrdDate(page);
        canvasModel.setPropertyOrdering(propOrdering);
        orderedPageRepository.save(canvasModel);
    }
    private Property propOrdSelect(OrderedPage page){
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Select select = (Select) project.getProperties().stream().filter(p -> p instanceof Select).findFirst().orElse(null);
        if(select == null) {
            ArrayList<Option> options = new ArrayList<>();
            options.add(new Option(null, "To-do", "#FF7A00"));
            options.add(new Option(null, "Doing", "#F7624B"));
            options.add(new Option(null, "Done", "#F04A94"));
            ArrayList<Page> pages = new ArrayList<>();
            pages.add(page);
            select = new Select(null, "Stats", true, false,
                    options, TypeOfProperty.SELECT, pages, null);
            page.setProperties(new ArrayList<>());
            select = selectRepository.save(select);
            page.getProperties().add(select);
        }
        return select;
    }

    private Property propOrdDate(OrderedPage page){
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Date date = (Date) project.getProperties().stream().filter(p -> p.getType().equals(TypeOfProperty.DATE)).findFirst().orElse(null);
        if(date == null) {
            ArrayList<Page> pages = new ArrayList<>();
            pages.add(page);
            date = new Date(null, "Date", true, false, false,false, false, false, "#F04A94", TypeOfProperty.DATE, pages, null);
            page.setProperties(new ArrayList<>());
            date = dateRepository.save(date);
            page.getProperties().add(date);
        }
        return date;
    }

    public void save(CanvasPage canvasPageModel) {
        canvasPageRepository.save(canvasPageModel);
    }

    public void save(OtherPage otherPage) {
        otherPageRepository.save(otherPage);
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

    public void merge(Collection<Page> pages, Long id) {
        Page page = pageRepository.findById(id).get();
        pages.stream().forEach(p -> {
            page.getTasks().stream().forEach(t -> {
                taskService.addTaskToPage(t.getTask(), p.getId());
            });
        });
    }
}
