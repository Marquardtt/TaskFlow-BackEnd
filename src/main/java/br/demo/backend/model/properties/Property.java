package br.demo.backend.model.properties;

import br.demo.backend.model.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_property")
@Inheritance(strategy = InheritanceType.JOINED)
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean visible;
    private Boolean obligatory;
    @Enumerated(value = EnumType.STRING)
    private Type type;
}
