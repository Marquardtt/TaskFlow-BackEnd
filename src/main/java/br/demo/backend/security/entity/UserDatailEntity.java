package br.demo.backend.security.entity;

import br.demo.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@Builder
public class UserDatailEntity implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    @OneToOne(mappedBy = "userDetailsEntity")
    @JsonIgnore
    private User user;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}