package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.values.DeserializerValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_property")
@Inheritance(strategy = InheritanceType.JOINED)
public class Property {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Boolean visible = true;
    private Boolean obligatory = false;
    @Enumerated(value = EnumType.STRING)
    private TypeOfProperty type;
 
    @ManyToMany
    private Collection<Page> pages;

    @ManyToOne
    @JsonIgnore
    private Project project;

    public Property(Long id){
        this.id = id;
    }
}
