package br.demo.backend.model.pages;


import br.demo.backend.model.properties.DeserializerProperty;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.enums.TypeOfPage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_page")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonDeserialize(using = DeserializePage.class)
public abstract class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private TypeOfPage type;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Property> properties;

    public Page(Long id){
        this.id = id;
    }
}
