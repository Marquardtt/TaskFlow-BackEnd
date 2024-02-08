package br.demo.backend.model.properties;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_date")
public class Date extends Property {

    private Boolean canBePass;
    private Boolean includesHours;
    private Boolean deadline;
    private Boolean scheduling;

}