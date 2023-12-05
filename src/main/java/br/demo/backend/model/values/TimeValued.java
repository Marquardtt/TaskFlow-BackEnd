package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_time_valued")
public class TimeValued extends Value {
    private LocalTime time;

    public TimeValued(Long id, LocalTime time) {
        super(id);
        this.time = time;
    }

    @Override
    public Object getValue() {
        return this.time;
    }
}
