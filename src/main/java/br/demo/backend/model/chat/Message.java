package br.demo.backend.model.chat;

import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(length = 1000)
    private String value;
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private User sender;
    private OffsetDateTime dateCreate = OffsetDateTime.now();
    private OffsetDateTime dateUpdate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private Collection<Destination> destinations;
    @OneToOne(cascade = CascadeType.ALL)
    private Archive annex;
}
