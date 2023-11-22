package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Select;
import br.demo.backend.service.properties.SelectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/select")
public class SelectController {
    private SelectService selectService;
    @PostMapping
    public void insert(@RequestBody Select select){
        selectService.save(select);
    }

    @PutMapping
    public void upSelect(@RequestBody Select select){
        selectService.save(select);
    }

    @GetMapping("/{id}")
    public Select findOne(@PathVariable Long id){
        return selectService.findOne(id);
    }

    @GetMapping
    public Collection<Select> findAll(){
        return selectService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        selectService.delete(id);
    }

}
