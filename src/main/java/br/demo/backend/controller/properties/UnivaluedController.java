package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.service.properties.UnivaluedService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/univalued")
public class UnivaluedController {
    private UnivaluedService univaluedService;
    @PostMapping
    public void insert(@RequestBody Univalued univalued){
        univaluedService.save(univalued);
    }

    @PutMapping
    public void upUnivalued(@RequestBody Univalued univalued){
        univaluedService.save(univalued);
    }

    @GetMapping("/{taskId}/{propertyId}")
    public Univalued findOne(@PathVariable Long taskId, @PathVariable Long propertyId){
        return univaluedService.findOne(taskId, propertyId);
    }

    @GetMapping
    public Collection<Univalued> findAll(){
        return univaluedService.findAll();
    }

    @DeleteMapping("/{taskId}/{propertyId}")
    public void delete(@PathVariable Long taskId, @PathVariable Long propertyId){
        univaluedService.delete(taskId, propertyId);
    }

}
