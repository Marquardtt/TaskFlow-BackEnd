package br.demo.backend.model.properties.relations;

import br.demo.backend.model.Property;
import br.demo.backend.model.Task;
import br.demo.backend.model.User;
import br.demo.backend.model.properties.relations.ids.ValueId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user_value")
@IdClass(ValueId.class)
public class UserValue{

    @Id
    private Long taskId;
    @Id
    private Long propertyId;

    @ManyToMany
    private Collection<User> value;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    private Task task;
    @ManyToOne
    @JoinColumn(name = "propertyId", insertable = false, updatable = false)
    private Property property;

}