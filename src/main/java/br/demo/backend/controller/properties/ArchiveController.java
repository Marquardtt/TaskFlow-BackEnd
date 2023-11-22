package br.demo.backend.controller.properties;


import br.demo.backend.model.properties.Archive;
import br.demo.backend.service.properties.ArchiveService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/archive")
public class ArchiveController {

    private ArchiveService archiveService;
    @PostMapping
    public void insert(@RequestBody Archive archive){
        archiveService.save(archive);
    }

    @PutMapping
    public void upDate(@RequestBody Archive archive){
        archiveService.save(archive);
    }

    @GetMapping("/{id}")
    public Archive findOne(@PathVariable Long id){
        return archiveService.findOne(id);
    }

    @GetMapping
    public Collection<Archive> findAll(){
        return archiveService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        archiveService.delete(id);
    }

}
