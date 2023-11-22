package br.demo.backend.model.properties;

import br.demo.backend.model.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_text")
public class Text extends Property {
    private Integer maximum;
    private String value;
}
