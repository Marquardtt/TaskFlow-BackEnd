package br.demo.backend.model.chat;

import br.demo.backend.model.User;
import br.demo.backend.model.ids.DestinationId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_destination")
public class Destination {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private DestinationId id;
    @ManyToOne
    @MapsId("userId")
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private User user;
    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    @MapsId("messageId")
    @JsonIgnore
    private Message message;
    private Boolean visualized = false;

}
