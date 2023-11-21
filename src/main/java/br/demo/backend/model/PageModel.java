package br.demo.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "db_page")

public class PageModel {
    @Id
    private Long id;
    private String name;
    private String type ;

    @ManyToMany
    private Collection<TaskModel> tasks;
    @ManyToMany
    private Collection<PropertyModel> properties;
}
