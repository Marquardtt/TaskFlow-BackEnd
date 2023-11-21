package br.demo.backend.model.properties;

import br.demo.backend.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_property_user")
public class PropertyUser extends Property {
    private Integer maximum;
    @ManyToMany
    private Collection<User> value;
}
