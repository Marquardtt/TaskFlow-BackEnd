package br.demo.backend.model.chat;

import br.demo.backend.model.User;
import br.demo.backend.model.ids.DestinationId;
import jakarta.persistence.*;
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
    @MapsId("userUsername")
    private User user;
    @ManyToOne
    @MapsId("messageId")
    private Message message;
    private Boolean visualized = false;

}
