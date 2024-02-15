package br.demo.backend.model.dtos.pages.get;


import br.demo.backend.model.Archive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//CANVAS
public class CanvasPageGetDTO extends PageGetDTO {

    private Archive draw;
}
