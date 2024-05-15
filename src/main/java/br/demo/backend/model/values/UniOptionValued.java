package br.demo.backend.model.values;

import br.demo.backend.model.properties.Option;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_valued_uni_option")
public class UniOptionValued extends Value{

    @ManyToOne()
    private Option uniOption;

    public UniOptionValued(Long id, Option uniOption){
        super(id);
        this.uniOption = uniOption;
;    }

    @Override
    public void setValue(Object value){this.uniOption = (Option) value;}

    @Override
    public Object getValue(){
        return this.uniOption;
    }

}
