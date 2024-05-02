package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.values.DeserializerValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(nullable = false)
    private Boolean visible = true;
    @Column(nullable = false)
    private Boolean obligatory = false;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TypeOfProperty type;
 
    @ManyToMany
    private Collection<Page> pages;

    @JoinColumn(updatable = false)
    @ManyToOne
    private Project project;

    public Property(Long id){
        this.id = id;
    }
    public Property(Long id, TypeOfProperty type, String name){
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
