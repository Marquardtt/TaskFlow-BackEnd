package br.demo.backend.model.dtos.relations;

import br.demo.backend.model.dtos.user.SimpleUserGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.values.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserValuedGetDTO extends Value {
    private Collection<SimpleUserGetDTO> users;

    @Override
    public void setValue(Object value) {
        this.users = (Collection<SimpleUserGetDTO>) value;
    }

    @Override
    public Object getValue() {
        return users;
    }
}
