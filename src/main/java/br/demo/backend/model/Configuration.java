package br.demo.backend.model;

import br.demo.backend.model.enums.Theme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Falta usar o cérebro
    private Boolean notifications;
    private String primaryColor;
    private String secondaryColor;
    @Enumerated(value = EnumType.STRING)
    private Theme theme;
    private Integer fontSize;
    @OneToOne(mappedBy = "configuration")
    private User user;
    private Boolean libras;
    private Boolean textToSound;
}
