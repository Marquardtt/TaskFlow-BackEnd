package br.demo.backend.model.properties;

import br.demo.backend.model.PropertyModel;
import br.demo.backend.model.UserModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_property_user")
public class PropertyUserModel  extends PropertyModel {
    private Boolean single;
    private Integer limit;
    private Collection<UserModel> value;
}
