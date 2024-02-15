package br.demo.backend.model.dtos.pages.put;


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
public class OrderedPagePutDTO extends PagePutDTO {
    private PropertyGetDTO propertyOrdering;
}
