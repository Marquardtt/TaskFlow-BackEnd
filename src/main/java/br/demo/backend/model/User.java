package br.demo.backend.model;

import br.demo.backend.security.entity.UserDatailEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    private String name;
    private String surname;
    //Patch
    @OneToOne(cascade = CascadeType.ALL)
    private Archive picture = new Archive(null,  "picture", "jpg", new byte[0]);

    @Column(unique = true)
    private String mail;
    private String phone;
    private String description;
    private boolean authenticate;

    //Patch
    @Column(nullable = false)
    private Long points = 0L;
    //Patch
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Configuration configuration = new Configuration();
    @ManyToMany (fetch = FetchType.EAGER)
    private Collection<Permission> permissions;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDatailEntity userDetailsEntity;
    @OneToMany(mappedBy = "user")
    private Collection<Notification> notifications;
    public User (String username){
        this.userDetailsEntity.setUsername(username);
    }

    public User(Long id) {this.id = id;}

}