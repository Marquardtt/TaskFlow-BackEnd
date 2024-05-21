package br.demo.backend.model;

import br.demo.backend.model.chat.Message;
import br.demo.backend.interfaces.IHasProperties;
import br.demo.backend.interfaces.ILogged;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.tasks.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import br.demo.backend.model.relations.PropertyValue;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "tb_project")
public class Project implements ILogged, IHasProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description = "";
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture = new Archive(null, "picture", "jpg", new byte[0]);
    private LocalDate deadline;
    //Patch
    @NotNull
    @Column(nullable = false)
    private OffsetDateTime visualizedAt = OffsetDateTime.now();
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private User owner;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Collection<Page> pages;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Collection<Property> properties;
    //===================== Adições
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> comments;
    @JoinColumn(name = "project_id")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<PropertyValue> values;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Log> logs;
    //===================== /Adições

    private Boolean revision = true;
    public Project(Long id){
        this.id = id;
    }

    @Override
    public Collection<PropertyValue> getPropertiesValues() {
        return values;
    }
}