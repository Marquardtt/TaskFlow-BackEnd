package br.demo.backend.service.properties;


import br.demo.backend.globalfunctions.ModelToGetDTO;
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
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.properties.SelectRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

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
    private AutoMapper<Limited> autoMapperLimited;
    private AutoMapper<Select> autoMapperSelect;
    private AutoMapper<Date> autoMapperDate;


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

    public void saveSelect(Select property){
        selectRepository.save(property);
        setInTheTasksThatAlreadyExists(property);
    }

    public void updateLimited(Limited propertyDTO, Boolean patching) {
        Limited property = patching ? limitedRepository.findById(propertyDTO.getId()).get() : new Limited();
        autoMapperLimited.map(propertyDTO, property, patching, true);
        limitedRepository.save(property);
    }

    public void updateDate(Date propertyDTO, Boolean patching) {
        Date property = patching ? dateRepository.findById(propertyDTO.getId()).get() : new Date();
        autoMapperDate.map(propertyDTO, property, patching, true);
        dateRepository.save(property);
    }

    public void updateSelect(Select propertyDTO, Boolean patching) {
        Select property = patching ? selectRepository.findById(propertyDTO.getId()).get() : new Select();
        autoMapperSelect.map(propertyDTO, property, patching, true);
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
            propertyRepository.delete(property);
        } else {
            throw new RuntimeException("Property can't be deleted");
        }
    }

    private Boolean validateCanBeDeleted(Property property) {
        if (testIfIsSelectable(property)) {
            if (property.getPages() == null) {
                TypeOfProperty[] typesOfProperty = {TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG};
                TypeOfPage[] typesOfPage = {TypeOfPage.KANBAN};
                return testInProject(typesOfProperty, typesOfPage, property);
            } else {
                return testInPages(new TypeOfProperty[]{TypeOfProperty.SELECT, TypeOfProperty.RADIO, TypeOfProperty.CHECKBOX, TypeOfProperty.TAG}, new TypeOfPage[]{TypeOfPage.KANBAN}, property, property.getPages());
            }
        } else if (property.getType().equals(TypeOfProperty.DATE)) {
            if (property.getPages() == null) {
                TypeOfProperty[] typesOfProperty = {TypeOfProperty.DATE};
                TypeOfPage[] typesOfPage = {TypeOfPage.TIMELINE, TypeOfPage.CALENDAR};
                return testInProject(typesOfProperty, typesOfPage, property);
            } else {
                return testInPages(new TypeOfProperty[]{TypeOfProperty.DATE}, new TypeOfPage[]{TypeOfPage.TIMELINE, TypeOfPage.CALENDAR}, property, property.getPages());
            }
        }
        return true;
    }

    private Boolean testInPages (TypeOfProperty[] typesOfProperty,TypeOfPage[] typesOfPage, Property property, Collection<Page> pages){
        return pages.stream().allMatch(p -> {
            if (Arrays.stream(typesOfPage).toList().contains(p.getType())) {
                return p.getProperties().stream().anyMatch(prop -> !prop.equals(property) && Arrays.stream(typesOfProperty).toList().contains(prop.getType()));
            }
            return true;
        });
    }

    private Boolean testInProject (TypeOfProperty[] typesOfProperty,TypeOfPage[] typesOfPage, Property property){
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
