package br.demo.backend.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "user_id")
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupModel group;

    private Enum permissions;

}
