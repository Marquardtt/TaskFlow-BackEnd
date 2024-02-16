package br.demo.backend.controller.properties;

import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.properties.Date;
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

    //TODO: quando a property ser adicionada adicionala as tasks

    @PostMapping("/limited")
    public void save(@RequestBody Limited property){
        propertyService.saveLimited(property);
    }
    @PostMapping("/select")
    public void save(@RequestBody Select property){
        propertyService.saveSelect(property);
    }
    @PostMapping("/date")
    public void save(@RequestBody Date property){
        propertyService.saveDate(property);
    }

    @PutMapping("/limited")
    public void update(@RequestBody Limited property){
        propertyService.updateLimited(property, false);
    }
    @PutMapping("/select")
    public void update(@RequestBody Select property){
        propertyService.updateSelect(property, false);
    }
    @PutMapping("/date")
    public void update(@RequestBody Date property){
        propertyService.updateDate(property, false);
    }
    @PatchMapping("/limited")
    public void patch(@RequestBody Limited property){
        propertyService.updateLimited(property, true);
    }
    @PatchMapping("/select")

    public void patch(@RequestBody Select property){
        propertyService.updateSelect(property, true);
    }
    @PatchMapping("/date")

    public void patch(@RequestBody Date property){
        propertyService.updateDate(property, true);
    }

    @GetMapping("/{id}")
    public PropertyGetDTO findOne(@PathVariable Long id){
        return propertyService.findOne(id);
    }

    @GetMapping
    public Collection<PropertyGetDTO> findAll(){
        return propertyService.findAll();
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }


}
