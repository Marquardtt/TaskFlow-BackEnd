package br.demo.backend.model.pages;


import br.demo.backend.model.Archive;
import br.demo.backend.model.relations.TaskCanvas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_canvas_page")
//CANVAS
public class CanvasPage extends Page {

    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(nullable = false)
    private Archive draw = new Archive(null, "draw", "png", new byte[0]);
}
