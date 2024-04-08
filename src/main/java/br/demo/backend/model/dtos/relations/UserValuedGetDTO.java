package br.demo.backend.model.dtos.relations;

import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.values.Value;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserValuedGetDTO extends Value {
    private Collection<OtherUsersDTO> users;

    @Override
    public void setValue(Object value) {
        this.users = (Collection<OtherUsersDTO>) value;
    }

    @Override
    public Object getValue() {
        return users;
    }
}
