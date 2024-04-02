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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/property/project/{projectId}")
public class PropertyController {

    private PropertyService propertyService;

 //Precisa ter permissão de post no projeto
    @PostMapping("/limited")
    public ResponseEntity<Limited> save(@RequestBody Limited property){
        System.out.println(property);
        return new ResponseEntity<Limited>(propertyService.saveLimited(property), HttpStatus.CREATED);
    }
    //Precisa ter permissão de post no projeto
    @PostMapping("/select")
    public ResponseEntity<Select> save(@RequestBody Select property){
        System.out.println(property);
        return new ResponseEntity<>(propertyService.saveSelect(property), HttpStatus.CREATED);
    }
    //Precisa ter permissão de post no projeto
    @PostMapping("/date")
    public ResponseEntity<Date> save(@RequestBody Date property){
        System.out.println(property);
        return new ResponseEntity<>(propertyService.saveDate(property),HttpStatus.CREATED);
    }

    //Precisa ter permissão de put no projeto
    @PutMapping("/limited")
    public void update(@RequestBody LimitedGetDTO property){
        propertyService.updateLimited(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PutMapping("/select")
    public void update(@RequestBody SelectGetDTO property){
        propertyService.updateSelect(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PutMapping("/date")
    public void update(@RequestBody DateGetDTO property){
        propertyService.updateDate(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/limited")
    public void patch(@RequestBody LimitedGetDTO property){
        propertyService.updateLimited(property, true);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/select")
    public void patch(@RequestBody SelectGetDTO property){
        propertyService.updateSelect(property, true);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/date")
    public void patch(@RequestBody DateGetDTO property){
        propertyService.updateDate(property, true);
    }

    //Precisa ter permissão de delete no projeto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }


}
