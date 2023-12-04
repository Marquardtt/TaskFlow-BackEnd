package br.demo.backend.model.properties;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_limited")
@Inheritance(strategy = InheritanceType.JOINED)
public class Limited extends Property {
    private Integer maximum;
    public Limited(Long id, String name, Boolean visible, Boolean obligatory, Integer maximum){
        super(id, name, visible, obligatory, TypeOfProperty.DATE);
        this.maximum = maximum;
    }
}
