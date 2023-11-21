package br.demo.backend.model.properties;

import br.demo.backend.model.PropertyModel;
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
public class TimeModel  extends PropertyModel {
    private Integer limit;
    private LocalTime value;
}
