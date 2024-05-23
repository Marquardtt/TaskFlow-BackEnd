package br.demo.backend.controller;

import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.values.ArchiveValued;
import br.demo.backend.service.PropertyValueService;
import br.demo.backend.utils.IdProjectValidation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project/{projectId}")
@AllArgsConstructor
public class PropertyValueCotroller {
    private PropertyValueService propertyValueService;

    @PatchMapping("/property-value/{id}")
    private ArchiveValued updateArchived(@PathVariable Long projectId, @PathVariable Long id, @RequestParam(required = false) MultipartFile file){
        return propertyValueService.setArchived(file, id, true, projectId);
    }
    @PatchMapping("/task/property-value/{id}")
    private ArchiveValued updateArchivedInTask(@PathVariable Long projectId,@PathVariable Long id, @RequestParam(required = false) MultipartFile file){
        return propertyValueService.setArchived(file, id ,false, projectId);
    }
}
