package br.demo.backend.controller;


import br.demo.backend.model.CanvasModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CanvasModel findOne(@RequestParam Long id){
        return canvasService.findOne(id);
    }

    @GetMapping
    public List<CanvasModel> findAll(){
        return canvasService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam Long id){
        canvasService.delete(id);
    }

}
