package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_valued_text")
public class TextValued extends Value {
    private String text;

    public TextValued(Long id, String text){
        super(id);
        this.text = text;
    }
    @Override
    public Object getValue(){
        return this.text;
    }


    @Override
    public void setValue(Object value){this.text = (String)value;}

}
