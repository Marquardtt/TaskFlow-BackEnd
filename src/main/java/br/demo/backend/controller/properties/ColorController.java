package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Color;
import br.demo.backend.service.properties.ColorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/color")
public class ColorController {
    private ColorService colorService;
    @PostMapping
    public void insert(@RequestBody Color color){
        colorService.save(color);
    }

    @PutMapping
    public void upDate(@RequestBody Color color){
        colorService.save(color);
    }

    @GetMapping("/{id}")
    public Color findOne(@PathVariable Long id){
        return colorService.findOne(id);
    }

    @GetMapping
    public Collection<Color> findAll(){
        return colorService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        colorService.delete(id);
    }

}
