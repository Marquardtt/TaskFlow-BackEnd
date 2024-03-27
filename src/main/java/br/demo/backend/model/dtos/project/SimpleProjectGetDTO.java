package br.demo.backend.model.dtos.project;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;


@Data
@AllArgsConstructor
public class SimpleProjectGetDTO {

    private Long id;
    private String name;
    private String description;
    private Archive picture;
    private Integer progress;
    private Collection<Group> groups;
}
