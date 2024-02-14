package br.demo.backend.model.chat;

import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
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
    private String value;
    @ManyToOne
    private User sender;
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private Collection<Destination> destination;
    @OneToOne(cascade = CascadeType.ALL)
    private Archive annex;
}
