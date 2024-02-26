package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_time")
public class TimeValued extends Value {
    private Duration time;

    public TimeValued(Long id, Duration time) {
        super(id);
        this.time = time;
    }

    @Override
    public void setValue(Object value){this.time = (Duration) value;}
    @Override
    public Object getValue() {
        return this.time;
    }
}
