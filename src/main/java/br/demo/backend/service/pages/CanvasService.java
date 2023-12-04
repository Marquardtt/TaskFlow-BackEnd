package br.demo.backend.service.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.repository.pages.CanvasRepository;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CanvasService {

    private CanvasRepository canvasRepository;
    private PropertyService propertyService;

    public Collection<Canvas> findAll() {
        return canvasRepository.findAll();
    }

    public Canvas findOne(Long id) {
        return canvasRepository.findById(id).get();
    }

    public void save(Canvas canvasModel) {
        canvasRepository.save(canvasModel);
    }

    public void update(Canvas canvas) {
        Canvas oldCanvas = canvasRepository.findById(canvas.getId()).get();

        for(Property propNew : canvas.getProperties()){
            if(!testIfAlredyExistsProperty(propNew, oldCanvas)) {
                propertyService.setRelationsBetweenPropAndTasks(canvas, propNew);
            }
        }

        canvasRepository.save(canvas);
    }

    private Boolean testIfAlredyExistsProperty(Property property, Canvas canvas){
        for(Property prop : canvas.getProperties()){
            if(prop.getId().equals(property.getId())){
                return true;
            }
        }
        return false;
    }


    public void delete(Long id) {
        canvasRepository.deleteById(id);
    }
}
