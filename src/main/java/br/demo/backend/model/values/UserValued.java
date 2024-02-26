package br.demo.backend.model.values;


import br.demo.backend.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_user")
public class UserValued extends Value{

    @ManyToMany
    private Collection<User> users;

    public UserValued(Long id, List<User> users){
        super(id);
        this.users = users;
    }
    public UserValued(Long id){
        super(id);
    }
    @Override
    public Object getValue(){
        return this.users;
    }

    @Override
    public void setValue(Object value){this.users = (List<User>)value;}
}
