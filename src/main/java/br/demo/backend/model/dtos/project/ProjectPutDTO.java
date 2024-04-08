package br.demo.backend.model.dtos.project;

import br.demo.backend.model.Archive;
import br.demo.backend.model.chat.Message;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.PropertyValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
public class ProjectPutDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private Collection<Message> comments;
    private Collection<PropertyValue> values;
}