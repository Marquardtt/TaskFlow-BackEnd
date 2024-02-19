package br.demo.backend.model.dtos.project;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
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
    //TODO: Se mudar para project ter taskvalues tirar isso
    private LocalDate deadline;
}