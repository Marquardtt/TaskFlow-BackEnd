package br.demo.backend.security.entity;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private LocalDateTime lastPasswordEdition;
    private LocalDateTime whenHeTryDelete;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        if (!this.user.getPermissions().isEmpty()) {
            for (Permission permission : this.user.getPermissions()) {
                list.add(new SimpleGrantedAuthority("Project_" + permission.getProject().getId() + "_" + permission.getPermission().getMethod()));
            }
            return list;
        }
        return null;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}