package br.demo.backend.model.dtos.chat.get;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
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
public class MessageGetDTO {
    @EqualsAndHashCode.Include

    private Long id;
    private String value;
    private SimpleUserGetDTO sender;
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;
    private Collection<DestinationGetDTO> destinations;
    private Archive annex;
}
