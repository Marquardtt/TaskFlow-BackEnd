package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.service.properties.MultivaluedService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/multivalued")
public class MultivaluedController {
    private MultivaluedService multivaluedService;
    @PostMapping
    public void insert(@RequestBody Multivalued multivalued){
        multivaluedService.save(multivalued);
    }

    @PutMapping
    public void upMultivalued(@RequestBody Multivalued multivalued){
        multivaluedService.save(multivalued);
    }

    @GetMapping("/{taskId}/{propertyId}")
    public Multivalued findOne(@PathVariable Long taskId, @PathVariable Long propertyId){
        return multivaluedService.findOne(taskId, propertyId);
    }

    @GetMapping
    public Collection<Multivalued> findAll(){
        return multivaluedService.findAll();
    }

    @DeleteMapping("/{taskId}/{propertyId}")
    public void delete(@PathVariable Long taskId, @PathVariable Long propertyId){
        multivaluedService.delete(taskId, propertyId);
    }

}
