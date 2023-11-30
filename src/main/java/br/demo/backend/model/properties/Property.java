package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

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
        private TypeOfProperty type;
    @ManyToOne
    @JsonIgnore
    private Page page;
    @ManyToOne
    @JsonIgnore
    private Project project;
}
