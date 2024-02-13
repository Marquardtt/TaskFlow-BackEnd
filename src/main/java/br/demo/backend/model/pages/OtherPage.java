package br.demo.backend.model.pages;

import br.demo.backend.model.relations.TaskOrdered;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table(name = "tb_other_page")

//LIST, TABLE
public class OtherPage extends Page {

}
