package br.demo.backend.model.properties;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_date")
public class Date extends Property {

    private Boolean canBePass;
    private Boolean includesHours;
    private Boolean term;
    private Boolean scheduling;

    public Date(Long id, String name, Boolean visible, Boolean obligatory, Boolean canBePass,
                Boolean includesHours, Boolean term, Boolean scheduling){
        super(id, name, visible, obligatory, TypeOfProperty.DATE);
        this.canBePass = canBePass;
        this.includesHours = includesHours;
        this.term = term;
        this.scheduling = scheduling;
    }
}