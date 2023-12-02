package br.demo.backend.model;

import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private String name;
    private String description;
    private Date date;
    private String picture;
    private User owner;
    private Collection<Property> properties;
}