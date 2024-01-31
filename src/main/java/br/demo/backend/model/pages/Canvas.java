package br.demo.backend.model.pages;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "db_canvas")
public class Canvas extends Page {

    //Patch
    private String draw;
}
