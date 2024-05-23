package br.demo.backend.model.values;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_time")
public class TimeValued extends Value {
    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    private Intervals value = new Intervals();
    public TimeValued(Long id, Intervals interval) {
        super(id);
        this.value = interval;
    }

    @Override
    public void setValue(Object value){this.value = (Intervals) value;}

    @Override
    public Object getValue() {
        return this.value;
    }
}
