package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_number_valued")
public class NumberValued extends Value{
    private Integer number;

    public NumberValued(Long id, Integer number){
        super(id);
        this.number = number;
    }
    @Override
    public Object getValue(){
        return this.number;
    }
}
