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
    @Column()
    private String name;
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture = new Archive(null, "picture", "jpg", new byte[0]);
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Permission> permissions;
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private User owner;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> users;

}