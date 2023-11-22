package br.demo.backend.model.properties;

import br.demo.backend.model.Option;
import br.demo.backend.model.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tag")
public class Multivalued extends Property {
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Collection<Option> options;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "tb_value")
    private List<String> value = new ArrayList<>();
    //    @ManyToMany
//    private Collection<Option> values;
}
