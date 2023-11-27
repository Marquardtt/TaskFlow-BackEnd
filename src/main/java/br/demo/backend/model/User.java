package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String userName;
    private String address;
    private String picture;
    private String mail;
    private String phone;
    private String description;
    private Integer points;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Configuration configuration;

    public User (Long id){
        this.id = id;
    }
}