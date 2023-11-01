package br.demo.backend.controller;

import br.demo.backend.model.PropertyModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public PropertyModel findOne(@RequestParam Long id){
        return propertyService.findOne(id);
    }

    @GetMapping
    public List<PropertyModel> findAll(){
        return propertyService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        propertyService.delete(id);
    }
}
