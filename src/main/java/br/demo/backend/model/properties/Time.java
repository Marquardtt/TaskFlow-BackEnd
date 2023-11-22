package br.demo.backend.model.properties;

import br.demo.backend.model.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_time")
public class Time extends Property {
    private Integer maximum;
    private LocalTime value;
}
