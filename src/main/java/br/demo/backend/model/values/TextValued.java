package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_text_valued")
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

}
