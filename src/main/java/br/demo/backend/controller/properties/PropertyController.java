package br.demo.backend.controller.properties;

import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/property")
public class PropertyController {
    private PropertyService propertyService;


    @PostMapping()
    public void save(@RequestBody Limited property){
        propertyService.save(property);
    }

    @PutMapping
    public void upDate(@RequestBody Property property){
        propertyService.update(property);
    }

    @GetMapping("/{id}")
    public Property findOne(@PathVariable Long id){
        return propertyService.findOne(id);
    }

    @GetMapping
    public Collection<Property> findAll(){
        return propertyService.findAll();
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }


}
