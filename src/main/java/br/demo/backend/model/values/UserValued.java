package br.demo.backend.model.values;


import br.demo.backend.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user_valued")
public class UserValued extends Value{

    @ManyToMany
    private List<User> users;

    public UserValued(Long id, List<User> users){
        super(id);
        this.users = users;
    }

    @Override
    public Object getValue(){
        return this.users;
    }
}