package br.demo.backend.controller;

import br.demo.backend.model.PropertyModel;
import br.demo.backend.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/property")
public class PropertyController {
    private PropertyService propertyService;

    @PostMapping
    public void insert(@RequestBody PropertyModel property){
        propertyService.save(property);
    }

    @PutMapping
    public void upDate(@RequestBody PropertyModel property){
        propertyService.save(property);
    }

    @GetMapping("/{id}")
    public PropertyModel findOne(@PathVariable Long id){
        return propertyService.findOne(id);
    }

    @GetMapping
    public Collection<PropertyModel> findAll(){
        return propertyService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }
}
