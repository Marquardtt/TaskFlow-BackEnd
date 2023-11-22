package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Date;
import br.demo.backend.service.properties.DateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/date")
public class DateController {
    private DateService dateService;
    @PostMapping
    public void insert(@RequestBody Date date){
        dateService.save(date);
    }

    @PutMapping
    public void upDate(@RequestBody Date date){
        dateService.save(date);
    }

    @GetMapping("/{id}")
    public Date findOne(@PathVariable Long id){
        return dateService.findOne(id);
    }

    @GetMapping
    public Collection<Date> findAll(){
        return dateService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        dateService.delete(id);
    }

}
