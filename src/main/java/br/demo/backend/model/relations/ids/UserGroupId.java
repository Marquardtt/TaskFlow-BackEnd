package br.demo.backend.model.relations.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupId implements Serializable {
    private Long userId;
    private Long groupId;

}
