package br.demo.backend.model;

import br.demo.backend.model.enums.Permission;
import br.demo.backend.model.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_property")
@Inheritance(strategy = InheritanceType.JOINED)
public class PropertyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean visible;
    private Boolean obligatory;

    @Enumerated(value = EnumType.STRING)
    private Type type;
}
