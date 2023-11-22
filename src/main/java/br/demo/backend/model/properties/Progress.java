package br.demo.backend.model.properties;

import br.demo.backend.model.Property;
import br.demo.backend.model.enums.TypeOfProgress;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_progress")
public class Progress extends Property {
    private Integer maximum;
    @Enumerated(value = EnumType.STRING)
    private TypeOfProgress typeOfProgress;
    private Integer value;

}
