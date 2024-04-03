package br.demo.backend.service.pages;

import br.demo.backend.model.dtos.relations.TaskOrderedGetDTO;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.service.properties.DefaultPropsService;
import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.dtos.relations.TaskCanvasGetDTO;
import br.demo.backend.model.enums.TypeOfPage;
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
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import br.demo.backend.repository.relations.TaskOrderedRepository;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class PageService {

    private OrderedPageRepository orderedPageRepository;
    private CanvasPageRepository canvasPageRepository;
    private PageRepository pageRepository;
    private LimitedRepository limitedRepository;
    private PropertyRepository propertyRepository;
    private TaskCanvasRepository taskCanvasRepository;
    private SelectRepository selectRepository;
    private DateRepository dateRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;
    private TaskOrderedRepository taskOrderedRepository;
    private DefaultPropsService defaultPropsService;

    private AutoMapper<OrderedPage> autoMapperOrdered;
    private AutoMapper<CanvasPage> autoMapperCanvas;
    private AutoMapper<TaskCanvas> autoMapperTaskCanvas;
    private AutoMapper<Page> autoMapperOther;
    private AutoMapper<TaskOrdered> autoMapperTaskOrdered;

    public OrderedPageGetDTO updatePropertiesOrdering(Property prop, Long pageId) {
        OrderedPage page = orderedPageRepository.findById(pageId).get();
        page.setPropertyOrdering(prop);
        return (OrderedPageGetDTO) ModelToGetDTO.tranform(orderedPageRepository.save(page));
    }

    public PageGetDTO updateName(String name, Long id) {
        Page page = pageRepository.findById(id).get();
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
        switch (page.getType()) {
            //pages that have a draw
            case CANVAS -> {
                CanvasPage canvasModel = new CanvasPage();
                autoMapperCanvas.map(page, canvasModel, false);
                return ModelToGetDTO.tranform(canvasPageRepository.save(canvasModel));
            }
            //pages that don't have a specific attribute
            case TABLE, LIST -> {
                Page canvasModel = new Page();
                autoMapperOther.map(page, canvasModel, false);
                return ModelToGetDTO.tranform(pageRepository.save(canvasModel));
            }
            //pages that have a property ordering
            default -> {
                OrderedPage canvasModel = new OrderedPage();
                autoMapperOrdered.map(page, canvasModel, false);
                OrderedPage pageServed = orderedPageRepository.save(canvasModel);
                //setting the property ordering
                Property propOrdering = null;
                switch (page.getType()) {
                    case KANBAN -> propOrdering = propOrdSelect(pageServed);
                    case TIMELINE -> propOrdering = propOrdTime(pageServed);
                    default -> propOrdering = propOrdDate(pageServed);
                }
                canvasModel.setPropertyOrdering(propOrdering);
                return ModelToGetDTO.tranform(orderedPageRepository.save(canvasModel));
            }
        }
    }
    private Property propOrdSelect(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Select select = (Select) testType(project, TypeOfProperty.SELECT, TypeOfProperty.TAG,
                TypeOfProperty.CHECKBOX, TypeOfProperty.RADIO);
        if (select == null) {
            select = defaultPropsService.select(project, page);
        }
        return select;
    }
    private Property propOrdDate(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Date date = (Date) testType(project, TypeOfProperty.DATE);
        if (date == null) {
            date = defaultPropsService.date(project, page);
        }
        return date;
    }
    private Property propOrdTime(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Limited limited = (Limited) testType(project, TypeOfProperty.TIME);
        if (limited == null) {
            limited = defaultPropsService.limited(project, page);
        }
        return limited;
    }

    //that method return the first property of the types passed as parameter
    private Property testType(Project project, TypeOfProperty ...types){
        return project
                .getProperties()
                .stream()
                .filter(p -> List.of(types).contains(p.getType()))
                .findFirst().orElse(null);
    }

    public void delete(Long id) {
        Page page = pageRepository.findById(id).get();
        page.getProperties().stream().forEach(p -> {
            p.getPages().remove(page);
            propertyRepository.save(p);
        });
        pageRepository.deleteById(id);
    }


    public TaskCanvasGetDTO updateXAndY(TaskCanvas taskCanvas) {
        TaskCanvas oldTaskCanvas = taskCanvasRepository.findById(taskCanvas.getId()).get();
        autoMapperTaskCanvas.map(taskCanvas, oldTaskCanvas, true);
        return (TaskCanvasGetDTO) ModelToGetDTO.tranform(taskCanvasRepository.save(oldTaskCanvas));
    }
    public TaskOrderedGetDTO updateIndex(TaskOrdered taskOrdered) {
        TaskOrdered oldTaskOrdered = taskOrderedRepository.findById(taskOrdered.getId()).get();
        autoMapperTaskOrdered.map(taskOrdered, oldTaskOrdered, true);
        return (TaskOrderedGetDTO) ModelToGetDTO.tranform(taskOrderedRepository.save(oldTaskOrdered));
    }

    public CanvasPageGetDTO updateDraw(MultipartFile draw, Long id) {
        CanvasPage canvasPage = canvasPageRepository.findById(id).get();
        Archive archive = new Archive(draw);
        canvasPage.setDraw(archive);
        return (CanvasPageGetDTO) ModelToGetDTO.tranform(canvasPageRepository.save(canvasPage));
    }

    public Collection<PageGetDTO> merge(Collection<Page> pages, Long id) {
        Page page = pageRepository.findById(id).get();
        pages.forEach(p ->
        {
            //setting the tasks and properties of the page
            p.getProperties().addAll(page.getProperties());
            //saving the pages
            page.getTasks().forEach(t -> {
                taskService.addTaskToPage(t.getTask(), p.getId());
            });
        });
        return  pages.stream().map(p -> ModelToGetDTO.tranform(pageRepository.findById(p.getId()).get())).toList();
    }
}
