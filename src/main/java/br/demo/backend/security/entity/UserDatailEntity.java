package br.demo.backend.security.entity;

import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.security.utils.GetHisProjects;
import br.demo.backend.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String username;
    @Length(min = 8)
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "userDetailsEntity")
    @JsonIgnore
    @ToString.Exclude
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
        Collection<Project> projects = GetHisProjects.getHisProjects(this.user);
        if (!this.user.getPermissions().isEmpty()) {

            try {
                projects.removeAll(this.user.getPermissions().stream().map(Permission::getProject).toList());
            } catch (UnsupportedOperationException ignore) {
            }
            for (Permission permission : this.user.getPermissions()) {
                list.add(new SimpleGrantedAuthority("Project_" + permission.getProject().getId() + "_" + permission.getPermission().getMethod()));
            }
        }
        for (Project project : projects) {
            list.add(new SimpleGrantedAuthority("Project_" + project.getId() + "_GET_POST_PUT_DELETE_PATCH"));
        }

        return list;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}