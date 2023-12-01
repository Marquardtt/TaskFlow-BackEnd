package br.demo.backend.model.values;


import br.demo.backend.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user_valued")
public class UserValued extends Value{

    @ManyToOne
    private User value;
}
