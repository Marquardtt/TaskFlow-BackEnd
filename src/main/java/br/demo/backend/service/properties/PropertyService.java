package br.demo.backend.service.properties;


import br.demo.backend.utils.ModelToGetDTO;
import br.demo.backend.model.Project;
import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.OrderedPageRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.utils.AutoMapper;
import br.demo.backend.repository.relations.TaskValueRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PropertyService {

    private PropertyRepository propertyRepository;
    private ProjectRepository projectRepository;
    private PageRepository pageRepository;
    private TaskService taskService;
    private LimitedRepository limitedRepository;
    private TaskRepository taskRepository;
    private SelectRepository selectRepository;
    private DateRepository dateRepository;
    private AutoMapper<Limited> autoMapperLimited;
    private AutoMapper<Select> autoMapperSelect;
    private AutoMapper<Date> autoMapperDate;
    private OrderedPageRepository orderedPageRepository;
    private TaskValueRepository taskValueRepository;


    public PropertyGetDTO findOne(Long id) {
        return ModelToGetDTO.tranform(propertyRepository.findById(id).get());
    }

    public Collection<PropertyGetDTO> findAll() {
        return propertyRepository.findAll().stream().map(ModelToGetDTO::tranform).toList();
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
        limitedRepository.save(property);
        setInTheTasksThatAlreadyExists(property);
    }

    public void saveDate(Date property) {
        dateRepository.save(property);
        setInTheTasksThatAlreadyExists(property);
    }

    public void saveSelect(Select property) {
        selectRepository.save(property);
        setInTheTasksThatAlreadyExists(property);
    }

    public void updateLimited(LimitedGetDTO propertyDTO, Boolean patching) {
        Limited old = limitedRepository.findById(propertyDTO.getId()).get();
        Limited property = patching ? old : new Limited();
        autoMapperLimited.map(propertyDTO, property, patching, true);
        property.setType(old.getType());
        property.setPages(old.getPages());
        property.setProject(old.getProject());
        limitedRepository.save(property);
    }

    public void updateDate(DateGetDTO propertyDTO, Boolean patching) {
        Date old = dateRepository.findById(propertyDTO.getId()).get();
        Date property = patching ? old : new Date();
        autoMapperDate.map(propertyDTO, property, patching, true);
        property.setType(old.getType());
        property.setPages(old.getPages());
        property.setProject(old.getProject());
        dateRepository.save(property);
    }

    public void updateSelect(SelectGetDTO propertyDTO, Boolean patching) {
        Select old = selectRepository.findById(propertyDTO.getId()).get();
        Select property = patching ? old : new Select();
        autoMapperSelect.map(propertyDTO, property, patching, true);
        property.setType(old.getType());
        property.setPages(old.getPages());
        property.setProject(old.getProject());
        selectRepository.save(property);
    }

    private Property setRelationAtPage(Property property, Collection<Page> pages) {
        pages.stream().forEach(p -> {
            Page page = pageRepository.findById(p.getId()).get();
            page.getTasks().stream().map(tP -> {
                tP.getTask().getProperties().add(taskService.setTaskProperty(property));
                taskService.update(tP.getTask(), true);
                return tP;
            }).toList();
        });
        return property;
    }


    public void delete(Long id) {
        Property property = propertyRepository.findById(id).get();
        if (validateCanBeDeleted(property)) {
            orderedPageRepository.findAll().stream().filter(p ->
                    p.getPropertyOrdering().equals(property)).forEach(p -> {
                Property newPropOrd = null;
                if (property.getType().equals(TypeOfProperty.DATE)) {
                    newPropOrd = getOtherProp(p, property,
                            new TypeOfProperty[]{TypeOfProperty.DATE});
                    if (newPropOrd == null) {
                        newPropOrd = getOtherProp(p.getProject(), property,
                                new TypeOfProperty[]{TypeOfProperty.DATE});
                    }
                } else {
                    newPropOrd = getOtherProp(p, property,
                            new TypeOfProperty[]{TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG});
                    if (newPropOrd == null) {
                        newPropOrd = getOtherProp(p.getProject(), property,
                                new TypeOfProperty[]{TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG});
                    }
                }
                p.setPropertyOrdering(newPropOrd);
                orderedPageRepository.save(p);
            });
            taskRepository.findAll().stream().forEach(t -> {
                TaskValue taskValue = t.getProperties().stream().filter(p ->
                        p.getProperty().getId().equals(id)).findFirst().orElse(null);
                if(taskValue != null){
                    t.getProperties().remove(taskValue);
                    taskService.update(t, true);
                    taskValueRepository.deleteById(taskValue.getId());
                }
            });
            propertyRepository.delete(property);
        } else {
            throw new RuntimeException("Property can't be deleted");
        }
    }

    private Boolean validateCanBeDeleted(Property property) {
        if (testIfIsSelectable(property)) {
            if (property.getProject() != null) {
                TypeOfProperty[] typesOfProperty = {TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG};
                TypeOfPage[] typesOfPage = {TypeOfPage.KANBAN};
                return testInProject(typesOfProperty, typesOfPage, property);
            } else {
                return testInPages(new TypeOfProperty[]{TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG}, new TypeOfPage[]{TypeOfPage.KANBAN}, property, property.getPages());
            }
        } else if (property.getType().equals(TypeOfProperty.DATE)) {
            if (property.getProject() != null) {
                TypeOfProperty[] typesOfProperty = {TypeOfProperty.DATE};
                TypeOfPage[] typesOfPage = {TypeOfPage.TIMELINE, TypeOfPage.CALENDAR};
                return testInProject(typesOfProperty, typesOfPage, property);
            } else {
                return testInPages(new TypeOfProperty[]{TypeOfProperty.DATE}, new TypeOfPage[]{TypeOfPage.TIMELINE, TypeOfPage.CALENDAR}, property, property.getPages());
            }
        }
        return true;
    }

    private Boolean testInPages(TypeOfProperty[] typesOfProperty, TypeOfPage[] typesOfPage, Property property, Collection<Page> pages) {
        return pages.stream().allMatch(p -> {
            if (Arrays.stream(typesOfPage).toList().contains(p.getType())) {
                return getOtherProp(p, property, typesOfProperty) != null;
            }
            return true;
        });
    }

    private Property getOtherProp(Page p, Property property, TypeOfProperty[] typesOfProperty) {
        return p.getProperties().stream().filter(prop ->
                !prop.equals(property) && Arrays.stream(typesOfProperty).toList().contains(prop.getType())).findFirst().orElse(null);
    }

    private Property getOtherProp(Project p, Property property, TypeOfProperty[] typesOfProperty) {
        return p.getProperties().stream().filter(prop ->
                !prop.equals(property) && Arrays.stream(typesOfProperty).toList().contains(prop.getType())).findFirst().orElse(null);
    }

    private Boolean testInProject(TypeOfProperty[] typesOfProperty, TypeOfPage[] typesOfPage, Property property) {
        Project project = projectRepository.findById(property.getProject().getId()).get();
        if (project.getProperties().stream().anyMatch(p -> !p.equals(property) && Arrays.stream(typesOfProperty).toList().contains(p.getType()))) {
            return true;
        } else return testInPages(typesOfProperty, typesOfPage, property, project.getPages());
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
