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
        ResolveStackOverflow.resolveStackOverflow(property);
        return property;
    }

    public Collection<Property> findAll() {
        Collection<Property> properties = propertyRepository.findAll();
        for (Property property : properties) {
            ResolveStackOverflow.resolveStackOverflow(property);
        }
        return properties;
    }

    public void update(Property property) {
        propertyRepository.save(property);
    }

    private void setInTheTasksThatAlreadyExists(Property property) {
        if (property.getPages() != null) {
            setRelationAtPage(property, property.getPages());
        } else {
            Project project = projectRepository.findById(property.getProject().getId()).get();
            setRelationAtPage(property, project.getPages());
        }
    }

    public void saveLimited(Limited property) {
        setInTheTasksThatAlreadyExists(property);
        limitedRepository.save(property);
    }

    public void saveDate(Date property) {
        setInTheTasksThatAlreadyExists(property);
        dateRepository.save(property);
    }

    public void saveSelect(Select property) {
        setInTheTasksThatAlreadyExists(property);
        selectRepository.save(property);
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


    private void setRelationAtPage(Property property, Collection<Page> pages) {
        for (Page pageJustId : pages) {
            Page page = pageRepository.findById(pageJustId.getId()).get();

            for (TaskPage taskCanvas : page.getTasks()) {
                taskService.setTaskProperty(property, taskCanvas.getTask());
            }

        }
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
                for (Property prop : project.getProperties()) {
                    prop.setProject(null);
                }
                for (Property prop : project.getProperties()) {
                    if (!prop.getId().equals(property.getId()) &&
                            testIfIsSelectable(prop)) {
                        return true;
                    }
                }
                for (Page page : project.getPages()) {
                    if (!testIfPageHasOtherProperty(page, property)) {
                        return false;
                    }
                }
                return true;
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
        for (Property prop : page.getProperties()) {
            if (!prop.getId().equals(property.getId()) &&
                    testIfIsSelectable(prop)) {
                return true;
            }
        }
        return false;
    }

}
