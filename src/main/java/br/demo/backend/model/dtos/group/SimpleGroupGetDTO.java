package br.demo.backend.model.dtos.group;

import br.demo.backend.model.Archive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SimpleGroupGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Archive picture;
    private String description;
    private String ownerUsername;
}