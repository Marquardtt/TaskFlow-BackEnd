package br.demo.backend.model.dtos.chat.post;

import br.demo.backend.model.dtos.chat.get.DestinationGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MessagePostPutDTO {
    @EqualsAndHashCode.Include

    private Long id;
    private String value;
    private OtherUsersDTO sender;
    private Collection<DestinationGetDTO> destinations;

}
