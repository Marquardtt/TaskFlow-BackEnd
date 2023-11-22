package br.demo.backend.controller;


import br.demo.backend.model.Log;
import br.demo.backend.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/log")
public class LogController {
    private LogService logService;
    @PostMapping
    public void insert(@RequestBody Log log){
        logService.save(log);
    }

    @PutMapping
    public void upDate(@RequestBody Log log){
        logService.save(log);
    }

    @GetMapping("/{id}")
    public Log findOne(@PathVariable Long id){
        return logService.findOne(id);
    }

    @GetMapping
    public Collection<Log> findAll(){
        return logService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        logService.delete(id);
    }

}
