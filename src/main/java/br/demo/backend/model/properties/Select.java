package br.demo.backend.model.properties;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_select")
public class Select extends Property {
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<Option> options;


    public Select(Long id, String name, Boolean visible, Boolean obligatory, Collection<Option> options) {
        super(id, name, visible, obligatory, TypeOfProperty.SELECT, null);
        this.options = options;
    }
    public Select(Long id, String name, Boolean visible, Boolean obligatory,Page page, Collection<Option> options) {
        super(id, name, visible, obligatory, TypeOfProperty.SELECT, page);
        this.options = options;
    }

}
