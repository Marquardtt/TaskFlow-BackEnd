package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_property")
public class PropertyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @OneToMany( cascade = CascadeType.PERSIST)
    private Collection<OptionModel> options;
    //private Enum type;  Procurar saber como fazer Enum corretamente
}
