package br.demo.backend.security.entity;

import br.demo.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class UserDatailEntity implements UserDetails {


    @Id
    @EqualsAndHashCode.Include
    private String username;
    @Length(min = 8)
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "userDetailsEntity")
    @JsonIgnore
    private User user;
    private boolean enabled =true ;
    private boolean accountNonExpired= true ;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired= true;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}