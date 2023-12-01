package br.demo.backend.model.values;

import br.demo.backend.model.properties.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_multi_option_valued")
public class MultiOptionValued extends Value{
    @ManyToMany
    private Collection<Option> value;
}
