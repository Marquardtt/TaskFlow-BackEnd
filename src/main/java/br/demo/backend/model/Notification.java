package br.demo.backend.model;

import br.demo.backend.model.enums.TypeOfNotification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tb_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aux;
    private TypeOfNotification type;
    private String link;

    @ManyToOne
    @JsonIgnore
    private User user;
    private Boolean visualized;
    private Boolean clicked;
    private Long objId;
}
