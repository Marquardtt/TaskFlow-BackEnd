package br.demo.backend.model.properties;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_data")
public class Data extends Property {

    private Boolean canBePass;
    private Boolean includesHours;
    private Boolean term;
    private Boolean scheduling;
    private LocalDateTime value;
}
