package br.demo.backend.model.dtos.user;

import br.demo.backend.model.Archive;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPostDTO {

    @EqualsAndHashCode.Include
    @NonNull
    private String username;
    private String name;
    private String surname;
    @Length(min = 8)
    private String password;
    @Email
    private String mail;
}