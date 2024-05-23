package br.demo.backend.model.values;

import br.demo.backend.model.properties.Option;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_multi_option")
public class MultiOptionValued extends Value{

    @ManyToMany
    private List<Option> multiOptions;

    public MultiOptionValued(Long id, List<Option> archive){
        super(id);
        this.multiOptions = archive;
    }
    @Override
    public void setValue(Object value){this.multiOptions = (List<Option>) value;}
    @Override
    public Object getValue(){
        return this.multiOptions;
    }
}
