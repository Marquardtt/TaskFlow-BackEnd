package br.demo.backend.model.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.relations.TaskPage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_date")
public class Date extends Property {

    private Boolean canBePass = false;
    private Boolean includesHours =false;
    //prazo  (quando deve estar pronto)
    private Boolean deadline = true;
    //agendamento (quando deve ser feito)
    private Boolean scheduling = true;
    private String color = "#F04A94";

    public Date (Long id, String name, Boolean visible, Boolean obligatory, Collection<Page> page, Project project) {
        super(id, name, visible, obligatory, TypeOfProperty.DATE, page, project);
        this.canBePass = canBePass;
        this.includesHours = includesHours;
        this.deadline = deadline;
        this.scheduling = scheduling;
        this.color = color;
    }

    public Date(Long idprop, TypeOfProperty type, String name) {
        super(idprop, type, name);
    }

}