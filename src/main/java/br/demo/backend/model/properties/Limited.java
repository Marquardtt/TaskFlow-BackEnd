package br.demo.backend.model.properties;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_limited")
public class Limited extends Property {
    private Integer maximum;

    @Override
    public String toString() {
        return "Limited{" +
                "maximum=" + maximum +
                "} " + super.toString();
    }
}
