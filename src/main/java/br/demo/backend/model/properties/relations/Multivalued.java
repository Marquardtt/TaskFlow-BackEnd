package br.demo.backend.model.properties.relations;

import br.demo.backend.model.*;
import br.demo.backend.model.properties.relations.ids.ValueId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_multivalued")
@IdClass(ValueId.class)
public class Multivalued {


    @Id
    private Long taskId;
    @Id
    private Long propertyId;

    @ElementCollection()
    @Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
    private List<String> value = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    private Task task;
    @ManyToOne
    @JoinColumn(name = "propertyId", insertable = false, updatable = false)
    private Property property;
}
