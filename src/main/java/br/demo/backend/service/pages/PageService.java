package br.demo.backend.service.pages;

import br.demo.backend.model.dtos.relations.TaskOrderedGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.repository.relations.TaskPageRepository;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.service.properties.DefaultPropsService;
import br.demo.backend.utils.IdProjectValidation;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.dtos.relations.TaskCanvasGetDTO;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.*;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import br.demo.backend.repository.relations.TaskOrderedRepository;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class PageService {

    private OrderedPageRepository orderedPageRepository;
    private CanvasPageRepository canvasPageRepository;
    private PageRepository pageRepository;
    private PropertyRepository propertyRepository;
    private TaskCanvasRepository taskCanvasRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;
    private TaskOrderedRepository taskOrderedRepository;
    private TaskPageRepository taskPageRepository;
    private DefaultPropsService defaultPropsService;
    private IdProjectValidation validation;
    private AutoMapper<OrderedPage> autoMapperOrdered;
    private AutoMapper<CanvasPage> autoMapperCanvas;
    private AutoMapper<TaskCanvas> autoMapperTaskCanvas;
    private AutoMapper<Page> autoMapperOther;
    private AutoMapper<TaskOrdered> autoMapperTaskOrdered;

    public OrderedPageGetDTO updatePropertiesOrdering(Property prop, Long pageId, Long projectId) {
        OrderedPage page = orderedPageRepository.findById(pageId).get();
        validation.ofObject(projectId, page.getProject());
        page.setPropertyOrdering(prop);
        return (OrderedPageGetDTO) ModelToGetDTO.tranform(orderedPageRepository.save(page));
    }

    public PageGetDTO updateName(String name, Long id, Long projectId) {
        Page page = pageRepository.findById(id).get();
        validation.ofObject(projectId, page.getProject());
        page.setName(name);
        if (page instanceof CanvasPage canvasPage) {
            return ModelToGetDTO.tranform(canvasPageRepository.save(canvasPage));
        } else if (page instanceof OrderedPage orderedPage) {
            return ModelToGetDTO.tranform(orderedPageRepository.save(orderedPage));
        }
        return ModelToGetDTO.tranform(pageRepository.save(page));
    }


    public PageGetDTO save(PagePostDTO page, Long projectId) {
        page.setProject(projectRepository.findById(projectId).get());
        return switch (page.getType()) {
            //pages that have a draw
            case CANVAS -> ModelToGetDTO.tranform(saveSpecific(autoMapperCanvas, page, new CanvasPage()));
            //pages that don't have a specific attribute
            case TABLE, LIST -> ModelToGetDTO.tranform(saveSpecific(autoMapperOther, page, new Page()));

            //pages that have a property ordering
            default -> {
                OrderedPage saved = (OrderedPage) saveSpecific(autoMapperOrdered, page, new OrderedPage());
                //setting the property ordering
                generatePropertyOrdering(saved);
                yield ModelToGetDTO.tranform(orderedPageRepository.save(saved));
            }
        };
    }

    private void generatePropertyOrdering(OrderedPage saved) {
        Property propOrdering = null;
        switch (saved.getType()) {
            case KANBAN -> propOrdering = propOrdSelect(saved);
            case TIMELINE -> propOrdering = propOrdTime(saved);
            default -> propOrdering = propOrdDate(saved);
        }
        saved.setPropertyOrdering(propOrdering);
    }

    private <T> Page saveSpecific(AutoMapper<T> autoMapper, PagePostDTO page, T emptyModel) {
        autoMapper.map(page, emptyModel, false);
        return pageRepository.save((Page) emptyModel);
    }

    private Property propOrdSelect(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Select select = (Select) testType(project, TypeOfProperty.SELECT, TypeOfProperty.TAG,
                TypeOfProperty.CHECKBOX, TypeOfProperty.RADIO);
        if (select == null) {
            select = defaultPropsService.select(null, page);
        }
        return select;
    }

    private Property propOrdDate(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Date date = (Date) testType(project, TypeOfProperty.DATE);
        if (date == null) {
            date = defaultPropsService.date(null, page);
        }
        return date;
    }

    private Property propOrdTime(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Limited limited = (Limited) testType(project, TypeOfProperty.TIME);
        if (limited == null) {
            limited = defaultPropsService.limited(null, page);
        }
        return limited;
    }

    //that method return the first property of the types passed as parameter
    private Property testType(Project project, TypeOfProperty... types) {
        return project
                .getProperties()
                .stream()
                .filter(p -> List.of(types).contains(p.getType()))
                .findFirst().orElse(null);
    }

    public void delete(Long id, Long projectId) {
        Page page = pageRepository.findById(id).get();
        validation.ofObject(projectId, page.getProject());
        page.getProperties().stream().forEach(p -> {
            p.getPages().remove(page);
            propertyRepository.save(p);
        });
        pageRepository.deleteById(id);
    }


    public TaskPageGetDTO updateTaskPage(TaskPage taskPage, Long projectId) {
        TaskPage oldTaskPage = taskPageRepository.findById(taskPage.getId()).get();
        Page page = pageRepository.findByTasks_Task(oldTaskPage.getTask()).stream().findFirst().get();
        validation.ofObject(projectId, page.getProject());
        if (taskPage instanceof TaskCanvas taskCanvas) {
            ((TaskCanvas) oldTaskPage).setX(taskCanvas.getX());
            ((TaskCanvas) oldTaskPage).setY(taskCanvas.getY());
            return ModelToGetDTO.tranform(taskCanvasRepository.save((TaskCanvas) oldTaskPage));
        }
        ((TaskOrdered) oldTaskPage).setIndexAtColumn(((TaskOrdered) taskPage).getIndexAtColumn());
        return ModelToGetDTO.tranform(taskOrderedRepository.save((TaskOrdered) oldTaskPage));

    }

    public CanvasPageGetDTO updateDraw(MultipartFile draw, Long id, Long projectId) {
        CanvasPage canvasPage = canvasPageRepository.findById(id).get();
        validation.ofObject(projectId, canvasPage.getProject());
        Archive archive = new Archive(draw);
        canvasPage.setDraw(archive);
        return (CanvasPageGetDTO) ModelToGetDTO.tranform(canvasPageRepository.save(canvasPage));
    }

    public Collection<PageGetDTO> merge(Collection<Page> pages, Long id, Long projectId) {
        Page page = pageRepository.findById(id).get();
        validation.ofObject(projectId, page.getProject());
        pages.forEach(p ->
        {
            Page pageDataBase = pageRepository.findById(p.getId()).get();
            validation.ofObject(projectId, pageDataBase.getProject());
            //setting the tasks and properties of the page
            pageDataBase.getProperties().addAll(page.getProperties());
            //saving the pages
            page.getTasks().forEach(t -> {
                taskService.addTaskToPage(t.getTask(), pageDataBase);
            });
        });
        return pages.stream().map(p -> ModelToGetDTO.tranform(pageRepository.findById(p.getId()).get())).toList();
    }
}
