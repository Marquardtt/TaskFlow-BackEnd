package br.demo.backend.model.dtos.pages.get;


import br.demo.backend.model.dtos.properties.PropertyGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor

//KANBAN, TIMELINE, CALENDAR
public class OrderedPageGetDTO extends PageGetDTO {
    private PropertyGetDTO propertyOrdering;
}
