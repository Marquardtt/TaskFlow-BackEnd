package br.demo.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String name;
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture ;
    private String description;

    @ManyToMany
    private Collection<Permission> permissions;
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private User owner;
    @ManyToMany
    private Collection<User> users;

}