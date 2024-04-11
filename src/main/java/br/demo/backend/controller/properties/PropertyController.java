package br.demo.backend.controller.properties;

import br.demo.backend.model.dtos.properties.DateGetDTO;
import br.demo.backend.model.dtos.properties.LimitedGetDTO;
import br.demo.backend.model.dtos.properties.SelectGetDTO;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Select;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/property/project/{projectId}")
public class PropertyController {

    private PropertyService propertyService;

 //Precisa ter permissão de post no projeto
    @PostMapping("/limited")
    public ResponseEntity<Limited> save(@RequestBody Limited property){
        return new ResponseEntity<>(propertyService.save(property), HttpStatus.CREATED);
    }
    //Precisa ter permissão de post no projeto
    @PostMapping("/select")
    public ResponseEntity<Select> save(@RequestBody Select property){
        return new ResponseEntity<>(propertyService.save(property), HttpStatus.CREATED);
    }
    //Precisa ter permissão de post no projeto
    @PostMapping("/date")
    public ResponseEntity<Date> save(@RequestBody Date property){
        return new ResponseEntity<>(propertyService.save(property),HttpStatus.CREATED);
    }

    //Precisa ter permissão de put no projeto
    @PutMapping("/limited")
    public void update(@RequestBody Limited property){
        propertyService.update(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PutMapping("/select")
    public void update(@RequestBody Select property){
        propertyService.update(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PutMapping("/date")
    public void update(@RequestBody Date property){
        propertyService.update(property, false);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/limited")
    public void patch(@RequestBody Limited property){
        propertyService.update(property, true);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/select")
    public void patch(@RequestBody Select property){
        propertyService.update(property, true);
    }
    //Precisa ter permissão de put no projeto
    @PatchMapping("/date")
    public void patch(@RequestBody Date property){
        propertyService.update(property, true);
    }

    //Precisa ter permissão de delete no projeto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyService.delete(id);
    }


}
