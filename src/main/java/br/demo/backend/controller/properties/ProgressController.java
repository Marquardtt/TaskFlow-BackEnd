package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Progress;
import br.demo.backend.service.properties.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/progress")
public class ProgressController {
    private ProgressService progressService;
    @PostMapping
    public void insert(@RequestBody Progress progress){
        progressService.save(progress);
    }

    @PutMapping
    public void upProgress(@RequestBody Progress progress){
        progressService.save(progress);
    }

    @GetMapping("/{id}")
    public Progress findOne(@PathVariable Long id){
        return progressService.findOne(id);
    }

    @GetMapping
    public Collection<Progress> findAll(){
        return progressService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        progressService.delete(id);
    }

}
