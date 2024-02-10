package br.demo.backend.model.pages;


import br.demo.backend.model.Archive;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_canvas")
public class Canvas extends Page {

    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive draw;
}
