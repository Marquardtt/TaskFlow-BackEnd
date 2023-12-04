package br.demo.backend.model.pages;


import br.demo.backend.model.enums.TypeOfPage;
import br.demo.backend.model.properties.DeserializerProperty;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Task;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_common_page")
public class CommonPage extends Page {
    @OneToMany
    private Collection<Task> tasks;
    @ManyToOne
    private Property propertyOrdering;

    public CommonPage(Long id, String name, TypeOfPage type, Collection<Property> props, Collection<Task> tasks, Property propertyOrdering){
        super(id, name, type, props);
        this.tasks = tasks;
        this.propertyOrdering = propertyOrdering;
    }
}
