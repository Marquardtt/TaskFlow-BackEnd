package br.demo.backend.service.properties;


import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.PropertyRepository;
import br.demo.backend.repository.tasks.TaskRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PropertyService {

    private PropertyRepository propertyRepository;
    private ProjectRepository projectRepository;
    private PageRepository pageRepository;
    private TaskService taskService;

    public Property findOne(Long id) {
        Property property = propertyRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(property);
        return property;
    }
    public Collection<Property> findAll() {
        Collection<Property> properties = propertyRepository.findAll();
        for(Property property : properties) {
            ResolveStackOverflow.resolveStackOverflow(property);
        }
        return properties;
    }
    public void update(Property property) {
        propertyRepository.save(property);
    }
    public void save(Property property) {
        if(property.getPages() != null){
            setRelationAtPage(property, property.getPages());
        }else{
            Project project = projectRepository.findById(property.getProject().getId()).get();
            setRelationAtPage(property, project.getPages());
        }
        propertyRepository.save(property);
    }

    private void setRelationAtPage(Property property, Collection<Page> pages){
        for(Page pageJustId : pages) {
            Page page = pageRepository.findById(pageJustId.getId()).get();
            if(page instanceof Canvas canvas){
                for(TaskCanvas taskCanvas : canvas.getTasks()) {
                    taskService.setTaskProperty(property, taskCanvas.getTask());
                }
            }else{
                CommonPage commonPage = (CommonPage) page;
                for(Task task : commonPage.getTasks()) {
                    taskService.setTaskProperty(property, task);
                }
            }
        }
    }

    public void delete(Long id) {
        Property property = propertyRepository.findById(id).get();
        if (validateCanBeDeleted(property)) {
            propertyRepository.delete(property);
        }
        throw new RuntimeException("Property can't be deleted");
    }

    private Boolean validateCanBeDeleted(Property property) {
        if (testIfIsSelectable(property)) {
            Project project = projectRepository.findByPropertiesContaining(property);
            for(Property prop : project.getProperties()) {
                prop.setProject(null);
            }
            if(project != null) {
                for (Property prop  : project.getProperties()) {
                    if (!prop.getId().equals(property.getId()) &&
                            testIfIsSelectable(prop)) {
                        return true;
                    }
                }
                for(Page page : project.getPages()) {
                    if(!testIfPageHasOtherProperty(page, property)) {
                        return false;
                    }
                }
                return true;
            }
            Page page = pageRepository.findByPropertiesContaining(property);
            return testIfPageHasOtherProperty(page, property);
        }
        return false;
    }

    private Boolean testIfIsSelectable(Property property) {
        return property.getType().equals(TypeOfProperty.SELECT) ||
                property.getType().equals(TypeOfProperty.RADIO) ||
                property.getType().equals(TypeOfProperty.CHECKBOX) ||
                property.getType().equals(TypeOfProperty.TAG);
    }

    private Boolean testIfPageHasOtherProperty(Page page, Property property) {
        for (Property prop  : page.getProperties()) {
            if (!prop.getId().equals(property.getId()) &&
                    testIfIsSelectable(prop)) {
                return true;
            }
        }
        return false;
    }

}
