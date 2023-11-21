package br.demo.backend.controller;


import br.demo.backend.model.pages.CanvasModel;
import br.demo.backend.service.CanvasService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/canvas")
public class CanvasController {
    private CanvasService canvasService;
    @PostMapping
    public void insert(@RequestBody CanvasModel canvas){
        canvasService.save(canvas);
    }

    @PutMapping
    public void upDate(@RequestBody CanvasModel canvas){
        canvasService.save(canvas);
    }

    @GetMapping("/{id}")
    public CanvasModel findOne(@PathVariable Long id){
        return canvasService.findOne(id);
    }

    @GetMapping
    public Collection<CanvasModel> findAll(){
        return canvasService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        canvasService.delete(id);
    }

}
