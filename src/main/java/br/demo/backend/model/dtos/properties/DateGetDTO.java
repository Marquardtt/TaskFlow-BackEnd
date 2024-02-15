package br.demo.backend.model.dtos.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DateGetDTO extends PropertyGetDTO {

    private Boolean canBePass;
    private Boolean includesHours;
    //prazo  (quando deve estar pronto)
    private Boolean deadline;
    //agendamento (quando deve ser feito)
    private Boolean scheduling;
    private String color;
}