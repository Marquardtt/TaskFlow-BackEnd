package br.demo.backend.model.properties;

import br.demo.backend.model.PropertyModel;
import br.demo.backend.model.enums.TypeOfProgress;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_progress")
public class ProgressModel  extends PropertyModel {
    private Integer limit;
    private TypeOfProgress type;
    private Integer value;

}
