package br.demo.backend.model.relations;

import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.*;
import br.demo.backend.model.values.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_property_value")
@JsonDeserialize(using = DeserializerValue.class)
public class PropertyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    private Property property;
    
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Value value;

    public PropertyValue(PropertyValue first){
        this.property =  new Property(first.getProperty().getId());
        this.id = null;
        Object value = first.getValue().getValue();
        this.value = switch (first.getProperty().getType()){
            case DATE -> new DateValued(null, new DateWithGoogle(null, ((DateWithGoogle)value).getDateTime(), ((DateWithGoogle)value).getIdGoogle()) );
            case SELECT, RADIO -> new UniOptionValued(null, (Option) value);
            case TIME -> {
                Intervals valueInt = (Intervals) value;
                yield new TimeValued(null, new Intervals(valueInt));
            }
            case TEXT -> new TextValued(null, (String) value);
            case TAG, CHECKBOX ->new MultiOptionValued(null, new ArrayList<>((List<Option>) value));
            case USER -> new UserValued(null, (List<User>) value) ;
            case ARCHIVE -> {
                Archive valueArchv = (Archive) value;
                yield  new ArchiveValued(null, new Archive(null, valueArchv.getName(),
                        valueArchv.getType(), valueArchv.getData()));
            }
            case NUMBER, PROGRESS-> new NumberValued(null, (Double) value);
        };
    }
}
