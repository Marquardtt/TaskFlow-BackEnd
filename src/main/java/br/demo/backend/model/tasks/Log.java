package br.demo.backend.model.tasks;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.Action;
import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.values.Value;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    private OffsetDateTime datetime = OffsetDateTime.now();
    @OneToOne(cascade = CascadeType.ALL)
    private PropertyValue value;
}
