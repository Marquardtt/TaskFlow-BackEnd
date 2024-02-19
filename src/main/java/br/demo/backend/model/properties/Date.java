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

    private Boolean canBePass;
    private Boolean includesHours;
    //prazo  (quando deve estar pronto)
    private Boolean deadline;
    //agendamento (quando deve ser feito)
    private Boolean scheduling;
    private String color;

    public Date (Long id, String name, Boolean visible, Boolean obligatory, Boolean canBePass, Boolean includesHours, Boolean deadline, Boolean scheduling, String color, TypeOfProperty type, Collection<Page> page, Project project) {
        super(id, name, visible, obligatory, type, page, project);
        this.canBePass = canBePass;
        this.includesHours = includesHours;
        this.deadline = deadline;
        this.scheduling = scheduling;
        this.color = color;
    }

}