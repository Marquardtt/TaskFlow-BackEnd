package br.demo.backend.model.dtos.chat.get;

import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.ids.DestinationId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DestinationGetDTO {
    @EqualsAndHashCode.Include
    private DestinationId id;
    private SimpleUserGetDTO user;
    private Boolean visualized ;

}
