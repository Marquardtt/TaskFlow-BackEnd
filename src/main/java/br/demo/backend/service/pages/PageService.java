package br.demo.backend.service.pages;

import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ModelToGetDTO;
import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.pages.post.PagePostDTO;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskOrdered;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.values.MultiOptionValued;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.CanvasPageRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.repository.relations.TaskCanvasRepository;
import br.demo.backend.repository.relations.TaskOrderedRepository;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class PageService {

    //Todo: criar task page, canvas ou ordered de acordo como type

    private OrderedPageRepository orderedPageRepository;
    private CanvasPageRepository canvasPageRepository;
    private PageRepository pageRepository;
    private PropertyRepository propertyRepository;
    private TaskCanvasRepository taskCanvasRepository;
    private SelectRepository selectRepository;
    private DateRepository dateRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;
    private TaskOrderedRepository taskOrderedRepository;

    private AutoMapper<OrderedPage> autoMapperOrdered;
    private AutoMapper<CanvasPage> autoMapperCanvas;
    private AutoMapper<TaskCanvas> autoMapperTaskCanvas;
    private AutoMapper<Page> autoMapperOther;

    public Collection<PageGetDTO> findAll() {
        Collection<Page> pages = pageRepository.findAll();
        return pages.stream().map(ModelToGetDTO::tranform).toList();
    }

    public PageGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(pageRepository.findById(id).get());
    }

    public void updatePropertiesOrdering(Property prop, Long pageId) {
        OrderedPage page = orderedPageRepository.findById(pageId).get();
        page.setPropertyOrdering(prop);
        orderedPageRepository.save(page);
    }

    public OrderedPageGetDTO updateIndex(OrderedPage pageDto, Long taskId, Integer index, Integer columnChaged) {
        OrderedPage page = orderedPageRepository.findById(pageDto.getId()).get();
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();

        Object column = taskOld.getTask().getProperties().stream().filter(p ->
                p.getProperty().equals(page.getPropertyOrdering())).findFirst().get().getValue().getValue();
        if(!List.of(new TypeOfProperty[]{TypeOfProperty.CHECKBOX, TypeOfProperty.TAG}).contains(pageDto.getPropertyOrdering().getType())){
            Option singleOption = (Option) column;
            updateIndexesInAOption(page, singleOption, taskOld, index, columnChaged);
        }else{
            Collection<Option> columns = (Collection<Option>) column;
            for(Option o : columns){
                updateIndexesInAOption(page, o, taskOld, index, columnChaged);
            }
        }
        return (OrderedPageGetDTO) ModelToGetDTO.tranform(orderedPageRepository.findById(pageDto.getId()).get());
    }

    private void updateIndexesInAOption(OrderedPage page, Option columnOption, TaskPage taskOld, Integer index,
                                                     Integer columnChanged ){
        page.setTasks(page.getTasks().stream().map(t -> {
            TaskValue prop = t.getTask().getProperties().stream().filter(p ->
                    p.getProperty().equals(page.getPropertyOrdering()) &&
                            (p.getValue().getValue() == null && columnOption == null ||
                                    (p.getValue().getValue() instanceof Option && ((Option) p.getValue().getValue()).equals(columnOption))
                                    || (((Collection<Option>)p.getValue().getValue()).contains(columnOption))
                                    )
            ).findFirst().orElse(null);
            if (prop == null) return t;
            updateIndexVerification((TaskOrdered)taskOld, (TaskOrdered) t, index, columnChanged);
            taskOrderedRepository.save((TaskOrdered) t);

            return t;
        }).toList());
    }

    private void updateIndexVerification(TaskOrdered taskOld, TaskOrdered t, Integer index, Integer columnChaged) {
        boolean verification = taskOld.getIndexAtColumn() > index || columnChaged == 1;
        if (t.equals(taskOld)) {
            t.setIndexAtColumn(index + (verification ? 0 : 1));
        } else {
            if ((t.getIndexAtColumn() >= index && verification) || t.getIndexAtColumn() > index) {
                t.setIndexAtColumn(t.getIndexAtColumn() + 1);
            }
        }
    }

    public PageGetDTO updateIndex(Page pageDto, Long taskId, Integer index) {
        OrderedPage page = orderedPageRepository.findById(pageDto.getId()).get();
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        page.setTasks(page.getTasks().stream().map(t -> {
            updateIndexVerification((TaskOrdered) taskOld, (TaskOrdered) t, index, 0);
            return t;
        }).toList());
        return ModelToGetDTO.tranform(pageRepository.save(page));
    }


    public void update(String name, Long id) {
        Page page = pageRepository.findById(id).get();
        page.setName(name);
        if (page instanceof CanvasPage canvasPage) {
            canvasPageRepository.save(canvasPage);
        } else if (page instanceof OrderedPage orderedPage) {
            orderedPageRepository.save(orderedPage);
        } else {
            pageRepository.save(page);
        }
    }


    private Property propOrdSelect(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Select select = (Select) project.getProperties().stream().filter(p -> p instanceof Select).findFirst().orElse(null);
        if (select == null) {
            ArrayList<Option> options = new ArrayList<>();
            options.add(new Option(null, "To-do", "#FF7A00", 0));
            options.add(new Option(null, "Doing", "#F7624B", 1));
            options.add(new Option(null, "Done", "#F04A94", 2));
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

    private Property propOrdDate(OrderedPage page) {
        Project project = projectRepository.findById(page.getProject().getId()).get();
        Date date = (Date) project.getProperties().stream().filter(p -> p.getType().equals(TypeOfProperty.DATE)).findFirst().orElse(null);
        if (date == null) {
            ArrayList<Page> pages = new ArrayList<>();
            pages.add(page);
            date = new Date(null, "Date", true, false, pages, null);
            page.setProperties(new ArrayList<>());
            Date dateSaved = dateRepository.save(date);
            page.getProperties().add(dateSaved);
        }
        return date;
    }

    public PageGetDTO save(PagePostDTO page) {
        if (page.getType().equals(TypeOfPage.CANVAS)) {
            CanvasPage canvasModel = new CanvasPage();
            autoMapperCanvas.map(page, canvasModel, false);
            canvasPageRepository.save(canvasModel);
            return ModelToGetDTO.tranform(canvasModel);
        } else if (List.of(TypeOfPage.TABLE, TypeOfPage.LIST).contains(page.getType())) {
            Page canvasModel = new Page();
            autoMapperOther.map(page, canvasModel, false);
            pageRepository.save(canvasModel);
            return ModelToGetDTO.tranform(canvasModel);
        } else {
            OrderedPage canvasModel = new OrderedPage();
            autoMapperOrdered.map(page, canvasModel, false);
            OrderedPage pageServed = orderedPageRepository.save(canvasModel);
            Property propOrdering = page.getType().equals(TypeOfPage.KANBAN) ?
                    propOrdSelect(pageServed) : propOrdDate(pageServed);
            canvasModel.setPropertyOrdering(propOrdering);
            orderedPageRepository.save(canvasModel);
            return ModelToGetDTO.tranform(canvasModel);
        }
    }

    public void delete(Long id) {
        Page page = pageRepository.findById(id).get();
        page.getProperties().stream().forEach(p -> {
            p.getPages().remove(page);
            propertyRepository.save(p);
        });

        pageRepository.deleteById(id);
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
        pages.forEach(p ->
        {
            p.setTasks(new ArrayList<>());
            page.getTasks().forEach(t -> {
                taskService.addTaskToPage(t.getTask(), p.getId());
            });
        });
    }
}
