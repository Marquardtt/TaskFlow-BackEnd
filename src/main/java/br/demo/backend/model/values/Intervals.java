package br.demo.backend.model.values;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.boot.web.embedded.netty.NettyWebServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_interval")
@Data
public class Intervals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    private Duration time = new Duration();

    @OneToMany(cascade =  CascadeType.ALL)
    private Collection<DateTimelines> starts = new ArrayList<>();
    @OneToMany(cascade =  CascadeType.ALL)
    private Collection<DateTimelines> ends = new ArrayList<>();
    private String color = "#F04A94";

    public Intervals(Intervals valueInt) {
        this.id = null;
        this.time = new Duration(null, valueInt.getTime().getSeconds(), valueInt.getTime().getMinutes(), valueInt.getTime().getHours());
        this.starts = valueInt.getStarts().stream().map(DateTimelines::new).toList();
        this.ends = valueInt.getEnds().stream().map(DateTimelines::new).toList();
        this.color = valueInt.getColor();
    }
}
