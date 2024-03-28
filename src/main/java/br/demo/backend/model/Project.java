package br.demo.backend.model;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "tb_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture = new Archive(null, "picture", "jpg", new byte[0]);
    private LocalDate deadline;
    //Patch
    @NotNull
    @Column(nullable = false)
    private LocalDateTime visualizedAt = LocalDateTime.now();
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private User owner;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Collection<Page> pages;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Collection<Property> properties;
    public Project(Long id){
        this.id = id;
    }
}