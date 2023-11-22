package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Time;
import br.demo.backend.service.properties.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/time")
public class TimeController {
    private TimeService timeService;
    @PostMapping
    public void insert(@RequestBody Time time){
        timeService.save(time);
    }

    @PutMapping
    public void upTime(@RequestBody Time time){
        timeService.save(time);
    }

    @GetMapping("/{id}")
    public Time findOne(@PathVariable Long id){
        return timeService.findOne(id);
    }

    @GetMapping
    public Collection<Time> findAll(){
        return timeService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        timeService.delete(id);
    }

}
