package br.demo.backend.model;


import br.demo.backend.model.properties.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_page")

public class Page {
    @Id
    private Long id;
    private String name;
    private String type ;

    @ManyToMany
    private Collection<Task> tasks;
    @ManyToMany
    private Collection<Property> properties;
}
