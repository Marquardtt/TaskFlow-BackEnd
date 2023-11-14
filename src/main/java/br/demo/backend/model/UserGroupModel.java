package br.demo.backend.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.CollationElementIterator;
import java.util.Collection;

@Entity
@Table(name = "tb_user_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = UserGroupId.class)
public class UserGroupModel {
    @Id
    private Long userId;
    @Id
    private Long groupId;


    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private GroupModel group;
    @Enumerated(value = EnumType.STRING)
    private Collection<Permission> permissions;
}
