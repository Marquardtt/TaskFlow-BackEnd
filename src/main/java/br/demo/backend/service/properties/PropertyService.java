package br.demo.backend.service.properties;


import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.pages.PageRepository;
import br.demo.backend.repository.properties.PropertyRepository;
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
}
