package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_time_valued")
public class TimeValued extends Value {
    private LocalTime tempo;

    public TimeValued(Long id, LocalTime tempo) {
        super(id);
        this.tempo = tempo;
    }

    @Override
    public Object getValue() {
        return this.tempo;
    }
}
