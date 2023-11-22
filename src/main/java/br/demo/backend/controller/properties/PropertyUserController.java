package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.PropertyUser;
import br.demo.backend.service.properties.PropertyUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/propertyUser")
public class PropertyUserController {
    private PropertyUserService propertyUserService;
    @PostMapping
    public void insert(@RequestBody PropertyUser propertyUser){
        propertyUserService.save(propertyUser);
    }

    @PutMapping
    public void upPropertyUser(@RequestBody PropertyUser propertyUser){
        propertyUserService.save(propertyUser);
    }

    @GetMapping("/{id}")
    public PropertyUser findOne(@PathVariable Long id){
        return propertyUserService.findOne(id);
    }

    @GetMapping
    public Collection<PropertyUser> findAll(){
        return propertyUserService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        propertyUserService.delete(id);
    }

}
