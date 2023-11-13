package br.demo.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;


public enum Permission {
    READ,
    UPDATE,
    DELETE,
    CREATE

}
