package br.demo.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GeneratedColumn;
import org.hibernate.validator.constraints.Length;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_user")
public class User {

    @Id
//    @GeneratedValue(generator = "uuid")
    @EqualsAndHashCode.Include
    private String username;

    private String name;
    private String surname;
    @Length(min = 8)
    @Column(nullable = false)
    private String password;
    private String address;
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture;

    private String mail;
    private String phone;
    private String description;
    //Patch
    @Column(nullable = false)
    private Long points = 0L;
    //Patch
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Configuration configuration = new Configuration();
    @ManyToMany
    private Collection<Permission> permissions;
    public User (String username){
        this.username = username;
    }

}