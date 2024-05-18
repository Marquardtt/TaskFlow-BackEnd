package br.demo.backend.model.properties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_option")
public class Option {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Id
    private Long id;
    private String name;
    @NotNull
    @Column(nullable = false)
    private String color;
    @NotNull
    @Column(nullable = false)
    private Integer indexAtSelect = 0;

    public Option(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
