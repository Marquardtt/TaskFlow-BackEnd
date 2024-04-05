package br.demo.backend.model.dtos.tasks;

import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LogGetDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String description;
    private Action action;
    private OtherUsersDTO user;
    private LocalDateTime datetime;
}
