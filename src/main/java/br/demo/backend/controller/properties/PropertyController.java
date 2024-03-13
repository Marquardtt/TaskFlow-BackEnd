package br.demo.backend.controller.properties;

import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/property")
public class PropertyController {

    private PropertyService propertyService;

    @PostMapping("/limited")
    public ResponseEntity<Limited> save(@RequestBody Limited property){
        System.out.println(property);
        return new ResponseEntity<Limited>(propertyService.saveLimited(property), HttpStatus.CREATED);
    }
    @PostMapping("/select")
    public void save(@RequestBody Select property){
        System.out.println(property);
        propertyService.saveSelect(property);
    }
    @PostMapping("/date")
    public void save(@RequestBody Date property){
        System.out.println(property);
        propertyService.saveDate(property);
    }

    @PutMapping("/limited")
    public void update(@RequestBody LimitedGetDTO property){
        propertyService.updateLimited(property, false);
    }
    @PutMapping("/select")
    public void update(@RequestBody SelectGetDTO property){
        propertyService.updateSelect(property, false);
    }
    @PutMapping("/date")
    public void update(@RequestBody DateGetDTO property){
        propertyService.updateDate(property, false);
    }
    @PatchMapping("/limited")
    public void patch(@RequestBody LimitedGetDTO property){
        propertyService.updateLimited(property, true);
    }
    @PatchMapping("/select")

    public void patch(@RequestBody SelectGetDTO property){
        propertyService.updateSelect(property, true);
    }
    @PatchMapping("/date")

    public void patch(@RequestBody DateGetDTO property){
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
