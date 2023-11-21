package br.demo.backend.model.pages;


import br.demo.backend.model.PageModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_canvas")
public class CanvasModel extends PageModel {

    private String draw;
}
