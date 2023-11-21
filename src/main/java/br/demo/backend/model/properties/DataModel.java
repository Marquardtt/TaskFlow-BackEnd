package br.demo.backend.model.properties;

import br.demo.backend.model.PropertyModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_data")
public class DataModel extends PropertyModel {

    private Boolean canBePass;
    private Boolean includesHours;
    private Boolean term;
    private Boolean scheduling;
    private LocalDateTime value;
}
