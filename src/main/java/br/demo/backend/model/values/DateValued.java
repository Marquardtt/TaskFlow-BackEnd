package br.demo.backend.model.values;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_date")
public class DateValued extends Value{
    @OneToOne(cascade = CascadeType.ALL)
    private DateWithGoogle date;

    public DateValued(Long id, DateWithGoogle dateTime){
        super(id);
        this.date = dateTime;
    }

    @Override
    public void setValue(Object value){this.date = (DateWithGoogle) value;}
    @Override
    public Object getValue(){
        return this.date;
    }
}
