package br.demo.backend.controller.pages;


import br.demo.backend.model.pages.Canvas;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.service.pages.CanvasService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/canvas")
public class CanvasController {
    private CanvasService canvasService;
    @PostMapping
    public void insert(@RequestBody Canvas canvas){
        canvasService.save(canvas);
    }

    @PutMapping
    public void upDate(@RequestBody Canvas canvas){
        canvasService.update(canvas, false);
    }
    @PatchMapping
    public void patch(@RequestBody Canvas canvas){
        canvasService.update(canvas, true);
    }

    @PatchMapping("/XandY")
    public void upDate(@RequestBody TaskPage taskPage){
        canvasService.updateXAndY( taskPage);
    }


    @PatchMapping("/draw/{id}")
    public void upDateDraw(@RequestParam MultipartFile draw, @PathVariable Long id){
        canvasService.updateDraw(draw, id);
    }


    @GetMapping("/{id}")
    public Canvas findOne(@PathVariable Long id){
        return canvasService.findOne(id);
    }

    @GetMapping
    public Collection<Canvas> findAll(){
        return canvasService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        canvasService.delete(id);
    }

}
