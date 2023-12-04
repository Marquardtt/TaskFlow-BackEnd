package br.demo.backend.controller.properties;

import br.demo.backend.model.properties.Property;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/property")
public class PropertyController {
    private PropertyService propertyService;

    @PutMapping
    public void upDate(@RequestBody Property property){
        propertyService.update(property);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }
}
