package br.demo.backend.model.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_log")
public class Log {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String description;
    private Action action;
    @ManyToOne
    private User user;
    private LocalDateTime datetime;
}
