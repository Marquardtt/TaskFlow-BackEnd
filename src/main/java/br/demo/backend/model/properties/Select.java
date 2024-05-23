package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.relations.TaskPage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_select")
public class Select extends Property {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "select_id")
    //Patch
    private Collection<Option> options;

    public Select(Long id, String name, Boolean visible, Boolean obligatory,
                  Collection<Option> options, TypeOfProperty type, Collection<Page> page, Project project) {
        super(id, name, visible, obligatory, type, page, project);
        this.options = options;
    }

    public Select(Long idprop, TypeOfProperty type, String name) {
        super(idprop, type, name);
    }
}
