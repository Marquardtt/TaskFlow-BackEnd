package br.demo.backend.model.dtos.chat.get;

import br.demo.backend.model.Archive;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MessageGetDTO {
    @EqualsAndHashCode.Include

    private Long id;
    private String value;
    private OtherUsersDTO sender;
    private OffsetDateTime dateCreate;
    private OffsetDateTime dateUpdate;
    private Collection<DestinationGetDTO> destinations;
    private Archive annex;
}
