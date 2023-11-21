package br.demo.backend.model.properties;

import br.demo.backend.model.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_select")
public class Select extends Property {
    @OneToMany
    private Collection<Option> options;
    private String value;
}
