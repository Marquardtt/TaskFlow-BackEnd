package br.demo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exceptionMessage;
    private LocalDateTime timestamp;
    private String stackTrace;

    public ErrorLog(Exception e) {
        this.exceptionMessage = e.getMessage();
        this.timestamp = LocalDateTime.now();
    }

}
