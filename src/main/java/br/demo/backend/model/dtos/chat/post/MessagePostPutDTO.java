package br.demo.backend.model.dtos.chat.post;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.chat.get.DestinationGetDTO;
import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MessagePostPutDTO {
    @EqualsAndHashCode.Include

    private Long id;
    private String value;
    private SimpleUserGetDTO sender;
    private Collection<DestinationGetDTO> destinations;

}
