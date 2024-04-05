package br.demo.backend.model.dtos.project;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
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
    private Collection<SimpleGroupGetDTO> groups;
    private OtherUsersDTO owner;
}
