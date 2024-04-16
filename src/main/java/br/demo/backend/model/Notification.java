package br.demo.backend.model;

import br.demo.backend.model.enums.TypeOfNotification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_notification")

public class Notification  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private TypeOfNotification type;
    private String link;
    @ManyToOne
    @JsonIgnore
    private User user;
    private Boolean visualized;




}
