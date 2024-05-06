package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_limited")
public class Limited extends Property {
    private Long maximum;

//    @Override
//    public String toString() {
//        return "Limited{" +
//                "maximum=" + maximum +
//                "} " + super.toString();
//    }

    public Limited(Long id, String name, @NotNull Boolean visible,
                   @NotNull Boolean obligatory, @NotNull TypeOfProperty type,
                   Collection<Page> pages, Project project, Long maximum) {
        super(id, name, visible, obligatory, type, pages, project);
        this.maximum = maximum;
    }
    public Limited(Long idprop, TypeOfProperty type, String name) {
        super(idprop, type, name);
    }

}
