package br.demo.backend.model;


import br.demo.backend.model.enums.TypeOfPage;
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

public class Page {
    @Id
    private Long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private TypeOfPage type ;

    @ManyToMany
    private Collection<Task> tasks;
    @ManyToMany
    private Collection<Property> properties;
}
