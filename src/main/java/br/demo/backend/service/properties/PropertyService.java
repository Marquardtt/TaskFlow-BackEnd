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
    private TaskRepository taskRepository;

    public void update(Property property) {
        propertyRepository.save(property);
    }

    public void delete(Long id) {
        Property property = propertyRepository.findById(id).get();
        if (validateCanBeDeleted(property)) {
            propertyRepository.delete(property);
        }
        throw new RuntimeException("Property can't be deleted");
    }

    private boolean validateCanBeDeleted(Property property) {
        if (testIfIsSelectable(property)) {
            Project project = projectRepository.findByPropertiesContaining(property);
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

    private boolean testIfIsSelectable(Property property) {
        return property.getType().equals(TypeOfProperty.SELECT) ||
                property.getType().equals(TypeOfProperty.RADIO) ||
                property.getType().equals(TypeOfProperty.CHECKBOX) ||
                property.getType().equals(TypeOfProperty.TAG);
    }

    private boolean testIfPageHasOtherProperty(Page page, Property property) {
        for (Property prop  : page.getProperties()) {
            if (!prop.getId().equals(property.getId()) &&
                    testIfIsSelectable(prop)) {
                return true;
            }
        }
        return false;
    }

    public void setRelationsBetweenPropAndTasks(Project project, Property property){
        for(Page page : project.getPages()){
            setRelationsBetweenPropAndTasks(page, property);
        }
    }

    public void setRelationsBetweenPropAndTasks(Page page, Property property){
        if(page.getType().equals(TypeOfPage.CANVAS)){
            for(TaskCanvas taskCanvas : ((Canvas)page).getTasks()){
                Task task = taskCanvas.getTask();
                task.getProperties().add(new TaskValue(null, property, null));
                taskRepository.save(task);
            }
        }else{
            for(Task task : ((CommonPage)page).getTasks()){
                task.getProperties().add(new TaskValue(null, property, null));
                taskRepository.save(task);
            }
        }
    }

}
