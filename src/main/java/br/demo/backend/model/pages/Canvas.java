package br.demo.backend.model.pages;


import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_canvas")
public class Canvas extends Page {
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<TaskCanvas> tasks;
    private String draw;


}
