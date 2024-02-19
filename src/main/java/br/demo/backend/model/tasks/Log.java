package br.demo.backend.model.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_log")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Log {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private Action action;
    @ManyToOne
    private User user;
    private LocalDateTime datetime;
}
