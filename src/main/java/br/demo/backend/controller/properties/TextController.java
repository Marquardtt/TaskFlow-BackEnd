package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Text;
import br.demo.backend.service.properties.TextService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/text")
public class TextController {
    private TextService textService;
    @PostMapping
    public void insert(@RequestBody Text text){
        textService.save(text);
    }

    @PutMapping
    public void upText(@RequestBody Text text){
        textService.save(text);
    }

    @GetMapping("/{id}")
    public Text findOne(@PathVariable Long id){
        return textService.findOne(id);
    }

    @GetMapping
    public Collection<Text> findAll(){
        return textService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        textService.delete(id);
    }

}
