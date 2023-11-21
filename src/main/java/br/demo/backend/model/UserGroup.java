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
public class UserGroup {
    @Id
    private Long userId;
    @Id
    private Long groupId;


    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Group group;
    @ManyToOne
    private Permission permission;

}
