package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Limited;
import br.demo.backend.service.properties.LimitedService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/limited")
public class LimitedController {
    private LimitedService limitedService;
    @PostMapping
    public void insert(@RequestBody Limited limited){
        limitedService.save(limited);
    }

    @PutMapping
    public void upLimited(@RequestBody Limited limited){
        limitedService.save(limited);
    }

    @GetMapping("/{id}")
    public Limited findOne(@PathVariable Long id){
        return limitedService.findOne(id);
    }

    @GetMapping
    public Collection<Limited> findAll(){
        return limitedService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        limitedService.delete(id);
    }

}
