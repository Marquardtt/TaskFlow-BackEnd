package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_date_valued")
public class DateValued extends Value{

    private LocalDateTime data;
    public DateValued(Long id, LocalDateTime data){
        super(id);
        this.data = data;
    }
    @Override
    public Object getValue(){
        return this.data;
    }
}
