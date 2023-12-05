package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_date_valued")
public class DateValued extends Value{

    private LocalDateTime dateTime;
    public DateValued(Long id, LocalDateTime dateTime){
        super(id);
        this.dateTime = dateTime;
    }
    @Override
    public Object getValue(){
        return this.dateTime;
    }
}
