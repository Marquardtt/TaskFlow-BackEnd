package br.demo.backend.model.pages;


import br.demo.backend.model.Archive;
import br.demo.backend.model.relations.TaskCanvas;
import jakarta.persistence.*;
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
    private Archive draw;
}
