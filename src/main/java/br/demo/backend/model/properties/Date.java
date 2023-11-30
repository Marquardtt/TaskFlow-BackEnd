package br.demo.backend.model.properties;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_date")
public class Date extends Property {

    private Boolean canBePass;
    private Boolean includesHours;
    private Boolean term;
    private Boolean scheduling;
}