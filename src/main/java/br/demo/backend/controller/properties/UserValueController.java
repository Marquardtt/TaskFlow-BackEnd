package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.service.properties.UserValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user-value")
public class UserValueController {
    private UserValueService userValueService;
    @PostMapping
    public void insert(@RequestBody UserValue userValue){
        userValueService.save(userValue);
    }

    @PutMapping
    public void upUserValue(@RequestBody UserValue userValue){
        userValueService.save(userValue);
    }

    @GetMapping("/{taskId}/{propertyId}")
    public UserValue findOne(@PathVariable Long taskId, @PathVariable Long propertyId){
        return userValueService.findOne(taskId, propertyId);
    }

    @GetMapping
    public Collection<UserValue> findAll(){
        return userValueService.findAll();
    }

    @DeleteMapping("/{taskId}/{propertyId}")
    public void delete(@PathVariable Long taskId, @PathVariable Long propertyId){
        userValueService.delete(taskId, propertyId);
    }

}
