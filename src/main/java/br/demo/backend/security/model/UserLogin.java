package br.demo.backend.security.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserLogin {
    private String username;
    private String mail;
    private String password;
    private boolean authenticate;
}
