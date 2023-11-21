package br.demo.backend.model.properties;

import br.demo.backend.model.PropertyModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_archive")
public class ArchiveModel extends PropertyModel {
    private Integer limit;
    private String value;
}
