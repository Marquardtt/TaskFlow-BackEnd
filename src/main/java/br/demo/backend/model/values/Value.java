package br.demo.backend.model.values;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_value")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonDeserialize(using = DeserializerValue.class)
public abstract class Value {
    @Id
    @GeneratedValue
    private Long id;

    public abstract Object getValue();
    public abstract void setValue(Object value);
}
