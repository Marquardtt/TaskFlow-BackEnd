package br.demo.backend.service.properties;


import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PropertyService {

    private PropertyRepository propertyRepository;
    private ProjectRepository projectRepository;
    private PageRepository pageRepository;
    private TaskService taskService;
    private LimitedRepository limitedRepository;
    private SelectRepository selectRepository;
    private DateRepository dateRepository;

    public Property findOne(Long id) {
        Property property = propertyRepository.findById(id).get();
        return ResolveStackOverflow.resolveStackOverflow(property);
    }

    public Collection<Property> findAll() {
        Collection<Property> properties = propertyRepository.findAll();
        return properties.stream().map(ResolveStackOverflow::resolveStackOverflow).toList();
    }

    public void update(Property property) {
        propertyRepository.save(property);
    }

    private Property setInTheTasksThatAlreadyExists(Property property) {
        if (property.getPages() != null) {
            return setRelationAtPage(property, property.getPages());
        } else {
            Project project = projectRepository.findById(property.getProject().getId()).get();
            return setRelationAtPage(property, project.getPages());
        }
    }

    public void saveLimited(Limited property) {
        limitedRepository.save((Limited)setInTheTasksThatAlreadyExists(property));
    }

    public void saveDate(Date property) {
        dateRepository.save((Date)setInTheTasksThatAlreadyExists(property));

    }

    public void saveSelect(Select property) {
        selectRepository.save((Select) setInTheTasksThatAlreadyExists(property));
    }
    public void updateLimited(Limited property) {
        limitedRepository.save(property);
    }

    public void updateDate(Date property) {
        dateRepository.save(property);
    }

    public void updateSelect(Select property) {
        selectRepository.save(property);
    }


    private Property setRelationAtPage(Property property, Collection<Page> pages) {
        pages.stream().map(p -> {
            Page page = pageRepository.findById(p.getId()).get();
            page.setTasks(page.getTasks().stream().map(tP -> {
                tP.getTask().getProperties().add(taskService.setTaskProperty(property));
                taskService.update(tP.getTask());
                return tP;
            }).toList());
            return page;
        });
        return property;
    }

    public void delete(Long id) {
        Property property = propertyRepository.findById(id).get();
        if (validateCanBeDeleted(property)) {
            propertyRepository.delete(property);
        }else{
        throw new RuntimeException("Property can't be deleted");
        }
    }

    private Boolean validateCanBeDeleted(Property property) {
        if (testIfIsSelectable(property)) {
            try {
                Project project = projectRepository.findById(property.getProject().getId()).get();
                if (project.getProperties().stream().anyMatch(p -> !p.getId().equals(property.getId()) &&
                        testIfIsSelectable(p))) return true;
                return project.getPages().stream().anyMatch(p -> testIfPageHasOtherProperty(p, property));
            } catch (NoSuchElementException e) {
                Page page = pageRepository.findByPropertiesContaining(property);
                return testIfPageHasOtherProperty(page, property);
            }
        }
        return true;
    }

    private Boolean testIfIsSelectable(Property property) {
        return property.getType().equals(TypeOfProperty.SELECT) ||
                property.getType().equals(TypeOfProperty.RADIO) ||
                property.getType().equals(TypeOfProperty.CHECKBOX) ||
                property.getType().equals(TypeOfProperty.TAG);
    }

    private Boolean testIfPageHasOtherProperty(Page page, Property property) {
        return page.getProperties().stream().anyMatch(p -> !p.getId().equals(property.getId()) &&
                testIfIsSelectable(p));
    }

}
