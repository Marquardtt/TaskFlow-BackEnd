package br.demo.backend.model;

import br.demo.backend.model.enums.TypeOfNotification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "tb_notification")

public class Notification  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aux;
    private TypeOfNotification type;
    private String link;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;
    private Boolean visualized;
    private Long auxObjId;
    private Long objId;
}
