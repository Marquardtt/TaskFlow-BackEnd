package br.demo.backend.controller;

import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.values.ArchiveValued;
import br.demo.backend.service.PropertyValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@AllArgsConstructor
public class PropertyValueCotroller {
    private PropertyValueService propertyValueService;

    @PatchMapping("/project/{projectId}/property-value/{id}")
    private ArchiveValued updateArchived(@PathVariable Long id, @RequestParam(required = false) MultipartFile file){
        return propertyValueService.setArchived(file, id, true);
    }
    @PatchMapping("/project/{projectId}/task/property-value/{id}")
    private ArchiveValued updateArchivedInTask(@PathVariable Long id, @RequestParam(required = false) MultipartFile file){
        return propertyValueService.setArchived(file, id ,false);
    }
}
