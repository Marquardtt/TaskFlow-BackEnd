package br.demo.backend.model.pages;


import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskOrdered;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table(name = "tb_ordered_page")

//KANBAN, TIMELINE, CALENDAR
public class OrderedPage extends Page {
    //Patch
    @ManyToOne
    @JoinColumn
    private Property propertyOrdering;


//    @Override
//    public String toString() {
//        return "OrderedPage{" +
//                "propertyOrdering=" + propertyOrdering +
//                "} " + super.toString();
//    }
}
