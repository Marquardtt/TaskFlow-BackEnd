package br.demo.backend.model.dtos.project;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.chat.get.MessageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import br.demo.backend.model.dtos.relations.PropertyValueGetDTO;
import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ProjectGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private Archive picture;
    private LocalDate deadline;
    private LocalDateTime visualizedAt;
    private SimpleUserGetDTO owner;
    private Collection<PageGetDTO> pages;
    private Collection<PropertyGetDTO> properties;
    private Collection<MessageGetDTO> comments;
    private Collection<PropertyValueGetDTO> values;

}