package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_date")
public class DateValued extends Value{

    private LocalDateTime dateTime;
    public DateValued(Long id, LocalDateTime dateTime){
        super(id);
        this.dateTime = dateTime;
    }

    @Override
    public void setValue(Object value){this.dateTime = (LocalDateTime) value;}
    @Override
    public Object getValue(){
        return this.dateTime;
    }
}
