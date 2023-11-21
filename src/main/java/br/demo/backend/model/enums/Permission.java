package br.demo.backend.model.enums;


import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;


public enum Permission {
    READ,
    UPDATE,
    DELETE,
    CREATE

}
