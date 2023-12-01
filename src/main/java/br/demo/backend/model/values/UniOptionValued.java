package br.demo.backend.model.values;

import br.demo.backend.model.properties.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_uni_option_valued")
public class UniOptionValued extends Value{

    @ManyToOne
    private Option value;

}
