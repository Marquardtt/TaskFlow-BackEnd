package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Multivalued;
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
    public void upDate(@RequestBody Multivalued multivalued){
        multivaluedService.save(multivalued);
    }

    @GetMapping("/{id}")
    public Multivalued findOne(@PathVariable Long id){
        return multivaluedService.findOne(id);
    }

    @GetMapping
    public Collection<Multivalued> findAll(){
        return multivaluedService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        multivaluedService.delete(id);
    }

}
